package solux.baco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import solux.baco.service.RouteModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RouteService {

    //여러 메서드에서 사용할 예정인 변수들이라 멤버변수로 선언
    List<List<Double>> path = null; //경로좌표 List의 List

    List<Double> startList = new ArrayList<>(); //(요청파라미터)시작좌표 List
    List<Double> endList = new ArrayList<>(); //(도착파라미터)도착좌표 List
    List<Double> startResponseList = new ArrayList<>(); //(네이버api응답)시작좌표 List
    List<Double> endResponseList = new ArrayList<>(); //(네이버api응답)도착좌표 List


    double[] startNaver = new double[2]; //네이버 호출 시 사용할 시작좌표 double배열
    double[] endNaver = new double[2]; //네이버 호출 시 사용할 도착좌표 double배열 

    //전체 메서드 실행 순서을 담고있는 메서드
    public Map<String, Object> passRouteData(double[] startKakao, double[] endKakao) {
        log.info("checkLog:RouteService - passRouteData called with startKakao: {} and endKakao: {}", startKakao, endKakao);


        //메서드 호출 시마다 startKakao,endKakao를 startList,endList에 매번 복사하면서 매번 배열에 같은 값이 추가되는 문제 발생
        //그래서 초기화해주는 코드 추가함.
        startList.clear();
        endList.clear();
        log.info("checkLog:RouteService - passRouteData called with startList(after clear): {} and endList(after clear): {}", startList, endList);

        for (double startCoordinate : startKakao) {
            startList.add(startCoordinate);
        }
        for (double endCoordinate : endKakao) {
            endList.add(endCoordinate);
        }
        log.info("checkLog:RouteService - passRouteData called with startList(after loop ): {} and endList(after loop ): {}", startList, endList);


        getLngLat(startKakao, endKakao);


        //2.네이버 지도 api 호출 후 응답받고 필요한 데이터만 변수에 담는 메서드 호출
        getRoute(startNaver, endNaver);
        log.info("checkLog:RouteService - passRouteData called with path(before swap): {}", path);

        //3. 경로 좌표 [경,위]에서 [위,경] 으로 변환
        swapPath();
        log.info("checkLog:RouteService - passRouteData called with path(after  swap): {}", path);

        //4.필요한 데이터들을 묶어서 객체형태로 만들기 위한 메서드 호출
        Map<String, Object> processRouteMap = processRoute();
        log.info("checkLog:RouteService - passRouteData called with processRouteMap: {}", processRouteMap);

        //5.경로좌표, 출발좌표, 도착좌표 담아서 json형태로 반환.
        return processRouteMap;
    }

    //1. 좌표데이터 순서 바꾸는 메서드
    public void getLngLat(double[] startKakao, double[] endKakao) {
        //출발좌표와 도착좌표 복사 (double[]형태에서 List<Double>형태로 만들기 위한 과정)


        //startKakao의 순서를 거꾸로=startNaver에 저장해야함
        startNaver[0]=startKakao[1];
        startNaver[1]=startKakao[0];

        endNaver[0]=endKakao[1];
        endNaver[1]=endKakao[0];
        log.info("checkLog:RouteService - getLngLat called with startNaver: {} and endNaver: {}", startNaver, endNaver);

    }


    //2. 네이버 지도 API(Direction 5 driving) 활용해서 길찾기 기능 수행 후 결과 얻는 메서드
    //좌표 형태의 출발지,도착지를 얻은 상태로 가정.
    public void getRoute(double[] startCoordinate, double[] endCoordinate) {

        //(1)네이버 지도 API 호출하는 과정
        //WebClient 인스턴스 생성
        WebClient webClient = WebClient.create();

        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving"; //네이버 api url
        String clientId = "73qkoqmj6s"; //네이버 지도 api 키 발급 id
        String clientSecret = "tKmrQEmfQ0rV0z7aTIO86uHnGL3bgfdw7hjy2YRt"; //네이버 지도 api 키 발급 pw
        double[] startParameter = startCoordinate; //ex-"127.12345, 37.12345"
        double[] endParameter = endCoordinate; //ex-"128.12345,38.12345"

        //요청 파라미터 설정 => url 쿼리 스트링 파라미터
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("start", startParameter[0] + "," + startParameter[1])
                .queryParam("goal", endParameter[0] + "," + endParameter[1])
                .queryParam("option", "trafast");

        //api 호출 후 응답받은 내용을 string 형태로 response에 저장.
        String response = webClient.get()
                .uri(uriBuilder.toUriString())
                .header("X-NCP-APIGW-API-KEY-ID", clientId) //요청헤더로 키 발급 id 전송
                .header("X-NCP-APIGW-API-KEY", clientSecret) //요청헤더로 키 발급 pw 전송
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("checkLog:RouteService - getRoute called with response: {}", response);

        //(2)응답 내용 중 필요한 정보 저장
        // Jackson ObjectMapper 인스턴스 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // readValue()메서드를 통해 json형식으로 구성된 string(response)을 java객체(JsonClass 클래스)에 매핑 후 JsonClass변수 jsonClass에 저장.
            JsonClass jsonClass = objectMapper.readValue(response, JsonClass.class);

            //jsonClass에 담긴 내용에서 getter를 통해 저장.
            int code = jsonClass.getCode(); //code
            String message = jsonClass.getMessage(); //message
            String currentDateTime = jsonClass.getCurrentDateTime(); //currentDateTime

            //중첩구조로 표현된 응답내용을 얻기 위해 인스턴스 생성(응답바디 구조 참고)
            RouteUnitEnt routeUnitEnt = jsonClass.getRoute(); //route

            //trafast속성은 배열형태이기 때문에 먼저 배열형태의 trafastList를 저장한 다음,
            //trafastList에서 0번째 요소(summary(start_location,goal_location,distance 등),path 등이 포함됨.)에 해당하는 내용을 trafast에 저장해야함.
            List<Trafast> trafastList = routeUnitEnt.getTrafast(); //trafastList
            Trafast trafast = trafastList.get(0);//trafast(trafastList의 0번째 요소)
            Summary summary = trafast.getSummary(); //summary

       startResponseList.add(0,summary.getStart().getLat());
            startResponseList.add(1,summary.getStart().getLng());

            endResponseList.add(0,summary.getGoal().getLat());
            endResponseList.add(1,summary.getGoal().getLng());

            log.info("checkLog:RouteService - getRoute called with startResponseList: {} and endResponseList : {} ", startResponseList,endResponseList);
            //경로 좌표 배열
            path = trafast.getPath();

            //거리 길이(추후에 필요할 수도 있어서 테스트해봄)
            int distance = summary.getDistance();
            //예외처리 구현예정
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void swapPath(){
        for (List<Double> coordinate : path) {
            double lng = coordinate.get(0);
            double lat = coordinate.get(1);

            coordinate.set(0,lat);
            coordinate.set(1,lng);
        }

    }




    //3.필요한 데이터 추출한 멤버변수를 객체형태로 만드는 단계의 메서드
    public Map<String, Object> processRoute() {
        Map<String, Object> processRouteData = new HashMap<>();

        processRouteData.put("start", startResponseList); //출발좌표
        processRouteData.put("path", path); //도착좌표 //swapPath로 바꿔야 하는 부분
        processRouteData.put("end", endResponseList); //도착좌표
        log.info("checkLog:RouteService - processRoute called with startList: {} and endList: {}", startList, endList);
        log.info("checkLog:RouteService - processRoute called with path: {}", path);

        return processRouteData; //Map<String, Object>형태로 반환하면 json으로 받을 수 있음.
    }

}