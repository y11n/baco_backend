package solux.baco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.service.MapHtmlService;
import solux.baco.service.MemberService;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewModel.ReviewDetailDTO;
import solux.baco.service.ReviewModel.returnReviewDataDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
import solux.baco.service.RouteModel.JsonDataEntity;
import solux.baco.service.RouteService;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/ReviewTest")

public class MapHtmlController {

    private final MapHtmlService mapHtmlService;
    private final returnReviewDataDTO returnReviewDataDTO;
    private final ReviewDetailDTO reviewDetailDTO;
    private final JsonDataEntity jsonDataEntity;

    @Autowired
    public MapHtmlController(MapHtmlService mapHtmlService, returnReviewDataDTO returnReviewDataDTO, ReviewDetailDTO reviewDetailDTO, JsonDataEntity jsonDataEntity) {
        this.mapHtmlService = mapHtmlService;
        this.returnReviewDataDTO = returnReviewDataDTO;
        this.reviewDetailDTO = reviewDetailDTO;
        this.jsonDataEntity = jsonDataEntity;

    }


    //@GetMapping("/test")
    public void dateTest() {
        /**
         //LocalDateTime 이용한 방법.
         LocalDateTime date = LocalDateTime.now();
         log.info("checklog: date = {}",date);
         */
    }


    //후기 및 경로 저장(후기작성)=>기본기능 구현 완료
    @PostMapping("/save")
    @ResponseBody //반환 타입을 바꿔야할지?
    public ResponseEntity<returnReviewDataDTO> saveReviewController(@RequestBody ReviewDTO reviewData) { //@RequestBody : 요청바디와 데이터 매핑.
        try {
            //log.info("checklog: email:{}, reviewData:{}",email,reviewData);
            //예외처리

            //1. ReviewDTO형태의 reviewData를 통해 startPlace,endPlace,content 추출.
            String startPlace = reviewData.getStartPlace();
            String endPlace = reviewData.getEndPlace();
            String content = reviewData.getContent();
            log.info("checklog: MapHtmlController_saveReviewController-startPlace:{},endPlace:{},content:{}", startPlace, endPlace, content);


            //(7/30) 1. 경로좌표전달 api호출로 경로데이터 얻기
            WebClient webClient = WebClient.create();

            String apiUrl = "http://localhost:8080/route"; //경로좌표전달 url => 서버 배포 시 url 변경 예정
            double[] startParameter = {37.54167, 126.964930}; //ex-"127.12345, 37.12345"
            double[] endParameter = {37.528409, 126.933089}; //ex-"128.12345,38.12345"

            //요청 파라미터 설정 => url 쿼리 스트링 파라미터
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("start", startParameter[0] + "," + startParameter[1])
                    .queryParam("end", endParameter[0] + "," + endParameter[1]);

            //api 호출 후 응답받은 내용을 string 형태로 routePoint에 저장.
            String routePointString = webClient.get()
                    .uri(uriBuilder.toUriString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("checklog: MapHtmlController_saveReviewController-routePoint:{}", routePointString);


            //html 동적 렌더링 코드 추가 예정. (일단 저장 구현부터..)
            String mapUrl = "테스트 중";
            log.info("checklog: MapHtmlController_saveReviewController-mapUrl: {}", mapUrl);
            //(7/30)2. 다른 데이터들 저장과 함께 경로좌표데이터도 저장 .

            //ReviewService 호출
            returnReviewDataDTO returnReviewDataDTO = mapHtmlService.saveReview(startPlace, endPlace, content, routePointString);
            return ResponseEntity.ok(returnReviewDataDTO);


        } catch (Exception e) {
            //db저장 실패 시
            return null; //예외처리 예정

        }

    }


    //후기 게시글 상세 조회
    @GetMapping("/detail/{review_id}")
    public ReviewDetailDTO reviewDetailContriller(@PathVariable Long review_id, Model model) {
        log.info("checklog: MapHtmlController_reviewDetailController");
        try {
            String mapUrl;
            //예외처리 구현 예정


            //html에 동적으로 렌더링하는 api를 호출 (MapTestController)
            WebClient webClient = WebClient.create();

            String apiUrl = "http://localhost:8080/mapTest"; //경로좌표전달 url => 서버 배포 시 url 변경 예정

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("review_id", review_id);

            String mapTest = webClient.get()
                    .uri(uriBuilder.toUriString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("checklog: MapHtmlController_reviewDetailController-mapTest:{}", mapTest);

            //페이지 반환 성공하면
            mapUrl = "http://localhost:8080/mapTest"; //수정 예정

            //html 렌더링 성공하면 =>
            //String serverUrl = "https://port-0-baco-server-eg4e2alkhufq9d.sel4.cloudtype.app/";
            //mapUrl = serverUrl + "mapTest"; //변경예정
            log.info("checklog: MapHtmlController_reviewDetailController-mapUrl:{}", mapUrl);
            // else {
            //mapUrl = "테스트 url";
            //log.info("checklog: MapHtmlController_reviewDetailController-mapUrl:{}", mapUrl);
            //}
            //review_id로 상세조회 시 필요한 데이터들 객체형태로 가져오는 service 메서드 호출.
            ReviewDetailDTO reviewDetail = mapHtmlService.reviewDetail(review_id, mapUrl);
            //log.info("checklog: ReviewController_reviewDetailContriller-reviewDetail:{}", review_id);

            //reviewDetailDTO의 mapUrl 에 setMapUrl(mapUrl)해서 설정 후 객체 형태로 반환.
            // (이후 프론트에서는 json에서 파싱해서 해당 url은 iframe으로 띄우고 다른 정보들도 각각 띄워줌.)
            return reviewDetail; //

        } catch (
                Exception e) {
            //예외처리 구현 예정
            return null;
        }


    }


}