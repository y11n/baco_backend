package solux.baco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import solux.baco.service.RouteModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class RouteService {

    //여러 메서드에서 사용할 예정인 변수들이라 멤버변수로 선언
    List<List<Double>> path=null;
    double[] startCoordinateArray;
    double[] endCoordinateArray;
    List<Double> startCoordinateCopy= new ArrayList<>();; //startCoordinateArray 형식은 제대로 값이 나타나지 않아서 다른 형식으로 데이터 복사
    List<Double> endCoordinateCopy= new ArrayList<>();;//endCoordinateArray 형식은 제대로 값이 나타나지 않아서 다른 형식으로 데이터 복사

    //전체 메서드 실행 순서을 담고있는 메서드
    public Map<String, Object> passRouteData(double[] startCoordinateArray,double[] endCoordinateArray) {

        /**
     //임시 데이터 (좌표데이터 받는 메서드 구현 예정)
         getCoordinate
*/
        //출발좌표와 도착좌표 복사
        for (double startCoordinate : startCoordinateArray) {
            startCoordinateCopy.add(startCoordinate);
        }
        for (double endCoordinate : endCoordinateArray) {
            endCoordinateCopy.add(endCoordinate);
        }

        //1.네이버 지도 api 호출 후 경로좌표데이터 받기
        getRoute(startCoordinateArray, endCoordinateArray);

        //2.데이터 가공
        Map<String, Object> processRouteMap = processRoute();

        return processRouteMap;
    }

    //1. 출발 도착 좌표 얻는 메서드 =>DTO 필요할 듯
    public void getCoordinate(String startPlace, String endPlace) {
        double[] startCoordinateArray;
        double[] endCoordinateArray;

    }


    //2. 네이버 지도 API(Direction 5 driving) 활용해서 길찾기 기능 수행 후 결과 얻는 메서드
    //좌표 형태의 출발지,도착지를 얻은 상태로 가정.
    public void getRoute(double[] startCoordinate, double[] endCoordinate) {

        //(1)네이버 지도 API 호출하는 과정
        //WebClient 인스턴스 생성
        WebClient webClient = WebClient.create();

        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving"; //네이버 api url
        String clientId = "73qkoqmj6s"; //네이버 지도 api 키 발급 id
        String clientSecret = "******"; //네이버 지도 api 키 발급 pw
        double[] startParameter = startCoordinate; //"127.12345, 37.12345"
        double[] endParameter = endCoordinate; //"128.12345,38.12345"

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

            startCoordinateArray = new double[2];
            startCoordinateArray[0] = summary.getStart().getLng();
            startCoordinateArray[1] = summary.getStart().getLat();

            endCoordinateArray = new double[2];
            endCoordinateArray[0] = summary.getGoal().getLng();
            endCoordinateArray[1] = summary.getGoal().getLat();

            //경로 좌표 배열
            path = trafast.getPath();

            //거리 길이(추후에 필요할 수도 있어서 테스트해봄)
            int distance = summary.getDistance();


        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }


    }

    public Map<String, Object> processRoute() {
        Map<String,Object> processRouteData = new HashMap<>();
        processRouteData.put("startCoordinate",startCoordinateCopy);
        processRouteData.put("path",path);
        processRouteData.put("endCoordinate",endCoordinateCopy);

        return processRouteData;
    }

}