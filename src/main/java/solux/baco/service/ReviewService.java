//origin/develop merge 테스트
package solux.baco.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.repository.ReviewRepository;
import solux.baco.service.ReviewModel.ReviewDetailDTO;
import solux.baco.service.ReviewModel.returnReviewDataDTO;
import solux.baco.service.RouteModel.JsonDataEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ReviewDetailDTO reviewDetailDTO;


    public ReviewService(ReviewRepository reviewRepository, MemberService memberService, ReviewDetailDTO reviewDetailDTO) {

        this.reviewRepository = reviewRepository;
        this.memberService = memberService;
        this.reviewDetailDTO = reviewDetailDTO;
    }


    //데이터 저장할 때
    @Transactional
    public returnReviewDataDTO saveReview(HttpSession session, String startPlace, String endPlace, String content, String routePoint) { //
        log.info("checklog: ReviewService_saveReview-review.setRoutePoint(routePoint): {}", startPlace);
        log.info("checklog: ReviewService_saveReview-review.setRoutePoint(routePoint): {}", endPlace);
        log.info("checklog: ReviewService_saveReview-review.setRoutePoint(routePoint): {}", content);
        log.info("checklog: ReviewService_saveReview-review.setRoutePoint(routePoint): {}", routePoint);


        Review review = new Review();
        returnReviewDataDTO returnReviewDataDTO = new returnReviewDataDTO();

        //1. 세션에서 이메일 추출하기
        String email = (String) session.getAttribute("loginEmail");
        log.info("checklog: ReviewService_saveReview-loginEmail : {}", email);
        //전달받은 데이터 예외처리
        log.info("checklog: ReviewService_saveReview-ReviewService");

        //2. 이메일을 통해서 작성자의 Member객체 받아오기
        Optional<Member> writerInfo = memberService.findByEmail(email);
        log.info("checklog: ReviewService_saveReview-writerInfo: {}", writerInfo);

        //3. 받아온 Member객체를 통해서 member_id 추출하기
        if (writerInfo.isPresent()) {
            //Optional 객체 속의 요소인 Member객체를 가져오기 위해 .get() //(null이 아닐 때만 get으로 가져올 수 있음.)
            Member member = writerInfo.get();
            //Long member_id = member.getMember_id();
            log.info("checklog: ReviewService_saveReview-review.routePoint: {}", routePoint);


            review.setMember(member); //작성자의 member테이블 레코드 저장
            review.setContent(content);
            review.setStartPlace(startPlace);
            review.setEndPlace(endPlace);
            review.setDate(LocalDate.now());
            review.setRoute_point(routePoint); //(7/30)수정-route테이블은 삭제, 후기테이블에 routePoint 컬럼 추가.

            log.info("checklog: ReviewService_saveReview-review.setRoutePoint(routePoint): {}", review.getRoute_point());

            Long review_id = reviewRepository.save(review);

            returnReviewDataDTO.setStartPlace(review.getStartPlace());
            returnReviewDataDTO.setEndPlace(review.getEndPlace());
            returnReviewDataDTO.setContent(review.getContent());
            returnReviewDataDTO.setReview_id(review_id);

        } else {
          return null;
        }
        return returnReviewDataDTO;
    }


    /**
     * 상세조회 관련 메서드
     */


    //컨트롤러에서 호출당하는메서드(경로데이터 빼올 때)
    public String getJsonData(Long review_id) {
        log.info("checklog: ReviewService_getJsonData");

        try {
            String routePointString = reviewRepository.routeData(review_id); //review_id로 route데이터 빼오는 과정
            log.info("checklog: ReviewService_getJsonData-routePointString: {}", routePointString);

            return routePointString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //(데이터 빼올 때 )
    public ReviewDetailDTO reviewDetail(Long review_id, String mapUrl) {
        ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
        log.info("checklog: ReviewService_getJsonData-review_id: {}", review_id);

        //review_id에 해당되는 저장값 가져오도록 repository 호출
        try {
            Optional<Review> reviewEntity = reviewRepository.detailReview(review_id);
            log.info("checklog: ReviewService_getJsonData-reviewEntity: {}", reviewEntity);

                // Optional이기 때문에 null일 수도 있음.
            if (reviewEntity.isPresent()) {
                Review review = reviewEntity.get();
                //String startPlace = review.getStartPlace();
                //String endPlace = review.getEndPlace();
                String content = review.getContent();
                //Long member_id = review.getMember().getMember_id();



                reviewDetailDTO.setContent(content);
                reviewDetailDTO.setMapUrl(mapUrl);
                //log.info("checklog: ReviewService_reviewDetail-startPlace: {}", startPlace);
                //log.info("checklog: ReviewService_reviewDetail-endPlace: {}", endPlace);
                log.info("checklog: ReviewService_getJsonData-content: {}", content);
                log.info("checklog: ReviewService_getJsonData-mapUrl: {}", mapUrl);





            }

        } catch (Exception e) {

            return null; //예외처리 구현예정
        }
        return reviewDetailDTO;
    }

    public Double[][] makeArray(String jsonData) {
        log.info("checklog: ReviewService_makeArray");

        JSONArray jsonArray = new JSONArray(jsonData);
        Double[][] jsonDataArray = new Double[jsonArray.length()][2];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray innerArray = jsonArray.getJSONArray(i);
            jsonDataArray[i][0] = innerArray.getDouble(0);
            jsonDataArray[i][1] = innerArray.getDouble(1);
        }
        log.info("checklog: ReviewService_makeArray-jsonDataArray:{}",jsonDataArray);
        return jsonDataArray;
    }
}

