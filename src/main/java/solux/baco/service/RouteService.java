package solux.baco.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import solux.baco.service.RouteModel.JsonClass;
import solux.baco.service.RouteModel.Route;
import reactor.core.publisher.Mono;


@Service
public class RouteService {
    /**
     * //1. 출발 도착 좌표 얻는 메서드
     * // 장소 이름 string으로 넘겨주는지, 장소 좌표 double 형태로 넘겨주는지?
     * public Coordinate getCoordinate(String startPlace, String endPlace) {
     * //로직 구현 예정
     *
     * }
     */
    //2. 네이버 지도 API(Direction 5 driving) 활용
    //좌표 형태의 출발지,도착지를 얻은 상태로 가정.
    public JsonClass getRoute(double[] startCoordinate, double[] endCoordinate) {
        //WebClient 인스턴스 생성
        WebClient webClient = WebClient.create();

        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
        String clientId = "73qkoqmj6s";
        String clientSecret = "*****";
        double[] startParameter = startCoordinate; //"127.12345, 37.12345"
        double[] endParameter = endCoordinate; //"128.12345,38.12345"


        //요청 파라미터 설정 => url 구성.
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("start", startParameter[0]+","+startParameter[1])
                .queryParam("goal", endParameter[0]+","+endParameter[1]);

        Mono<JsonClass> responseBodyMono = webClient.get()
                    .uri(uriBuilder.toUriString())
                    .header("X-NCP-APIGW-API-KEY-ID", clientId)
                    .header("X-NCP-APIGW-API-KEY", clientSecret)
                    .retrieve()
                    .bodyToMono(JsonClass.class);

        JsonClass responseBody = responseBodyMono.block();

        return responseBody;
        }



    }


