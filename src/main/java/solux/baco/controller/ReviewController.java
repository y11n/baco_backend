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
import solux.baco.service.MemberService;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewModel.ReviewDetailDTO;
import solux.baco.service.ReviewModel.returnReviewDataDTO;
import solux.baco.service.ReviewService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Review")

public class ReviewController {

    private final ReviewService reviewService;
    private final returnReviewDataDTO returnReviewDataDTO;
    private final ReviewDetailDTO reviewDetailDTO;
    private final JsonDataEntity jsonDataEntity;

    @Autowired
    public ReviewController(ReviewService reviewService, returnReviewDataDTO returnReviewDataDTO, ReviewDetailDTO reviewDetailDTO, JsonDataEntity jsonDataEntity) {
        this.reviewService = reviewService;
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
    public ResponseEntity<returnReviewDataDTO> saveReviewController(HttpSession session, @RequestBody ReviewDTO reviewData) { //@RequestBody : 요청바디와 데이터 매핑.
        try {
            //log.info("checklog: email:{}, reviewData:{}",email,reviewData);
            //예외처리

            //1. ReviewDTO형태의 reviewData를 통해 startPlace,endPlace,content 추출.
            String startPlace = reviewData.getStartPlace();
            String endPlace = reviewData.getEndPlace();
            String content = reviewData.getContent();
            log.info("checklog: startPlace:{},endPlace:{},content:{}", startPlace, endPlace, content);


            //(7/30) 1. 경로좌표전달 api호출로 경로데이터 얻기
            WebClient webClient = WebClient.create();

            String apiUrl = "https://port-0-baco-server-eg4e2alkhufq9d.sel4.cloudtype.app/route"; //경로좌표전달 url => 서버 배포 시 url 변경 예정
            double[] startParameter = {37.56640973022008, 126.97857314414601}; //ex-"127.12345, 37.12345"
            double[] endParameter = {37.57621220811897, 126.97672509786915}; //ex-"128.12345,38.12345"

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
            log.info("checklog: routePoint:{}", routePointString);


            //html 동적 렌더링 코드 추가 예정. (일단 저장 구현부터..)

            String mapUrl = "테스트 중";

            //(7/30)2. 다른 데이터들 저장과 함께 경로좌표데이터도 저장 .

            //ReviewService 호출
            returnReviewDataDTO returnReviewDataDTO = reviewService.saveReview(session,startPlace, endPlace, content, routePointString);
            return ResponseEntity.ok(returnReviewDataDTO);


        } catch (Exception e) {
            //db저장 실패 시
            return null; //예외처리 예정

        }

    }


    //후기 게시글 상세 조회
    @GetMapping("/detail/{review_id}")
    public ReviewDetailDTO reviewDetailContriller(@PathVariable Long review_id, Model model) {
        try {
            //예외처리 구현 예정
            log.info("checklog: ReviewController_reviewDetailContriller-reviewDetail:{}", review_id);


            JsonDataEntity jsonData = reviewService.getJsonData(review_id); //이 부분은 (상세조회컨트롤러에서) 후기와 함께 데이터 가져온 다음, jsonData변수에 넣어주는걸로 바꾸면 될 듯..
            if (jsonData != null) {
/**
                //JsonData를 html에 렌더링하기 위해 Thymeleaf 템플릿으로 전달. (=Thymeleaf를 통해 html로 전달)
                model.addAttribute("jsonData", jsonDataEntity.getRoutePoint()); //jsonDTO형태로 맞춰서 저장했다가 getter메서드로 가져와서 model로 전달하기.
                log.info("checklog: ReviewController_reviewDetailContriller-model.addAttribute");
                //html 렌더링 성공하면 =>
                String serverUrl = "https://port-0-baco-server-eg4e2alkhufq9d.sel4.cloudtype.app/";
                String mapUrl = serverUrl + "mapTest"; //변경예정
                log.info("checklog: ReviewController_reviewDetailContriller-reviewDetail:{}", mapUrl);
*/

                String mapUrl = "테스트 url";
                log.info("checklog: ReviewController_reviewDetailContriller-reviewDetail:{}", mapUrl);

                //review_id로 상세조회 시 필요한 데이터들 객체형태로 가져오는 service 메서드 호출.
                ReviewDetailDTO reviewDetail = reviewService.reviewDetail(review_id, mapUrl);
                //log.info("checklog: ReviewController_reviewDetailContriller-reviewDetail:{}", review_id);

                //reviewDetailDTO의 mapUrl 에 setMapUrl(mapUrl)해서 설정 후 객체 형태로 반환.
                // (이후 프론트에서는 json에서 파싱해서 해당 url은 iframe으로 띄우고 다른 정보들도 각각 띄워줌.)
                return reviewDetail; //


            }
        } catch (Exception e) {
            //예외처리 구현 예정
            return null;
        }

    return reviewDetailDTO; //수정해야할 듯.
    }

    //해시태그 필터링
    @GetMapping("/reviews")
    public List<Review> showReviews_hashtag(@RequestParam String hashtag){
        return reviewService.findHashtagReviews(hashtag);
    }

}
