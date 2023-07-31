//origin/develop merge 테스트
package solux.baco.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.repository.ReviewRepository;
import solux.baco.service.ReviewModel.ReviewDetailDTO;
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


    public ReviewService(ReviewRepository reviewRepository, MemberService memberService) {

        this.reviewRepository = reviewRepository;
        this.memberService = memberService;
    }


    //데이터 저장할 때
    @Transactional
    public ReviewDetailDTO saveReview( String startPlace, String endPlace, String content, String routePoint) {
        log.info("checklog: review.setRoutePoint(routePoint): {}", startPlace);
        log.info("checklog: review.setRoutePoint(routePoint): {}", endPlace);
        log.info("checklog: review.setRoutePoint(routePoint): {}", content);
        log.info("checklog: review.setRoutePoint(routePoint): {}", routePoint);


        Review review = new Review();
        ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
        String mapUrl;

        String email = "test2@test.com";
        log.info("checklog: ReviewService");
        //2. 이메일을 통해서 작성자의 Member객체 받아오기
        Optional<Member> writerInfo = memberService.findByEmail(email);
        log.info("checklog: writerInfo: {}", writerInfo);

        //3. 받아온 Member객체를 통해서 member_id 추출하기
        if (writerInfo.isPresent()) {
            //Optional 객체 속의 요소인 Member객체를 가져오기 위해 .get() //(null이 아닐 때만 get으로 가져올 수 있음.)
            Member member = writerInfo.get();
            //Long member_id = member.getMember_id();
            log.info("checklog: review.routePoint: {}", routePoint);


            review.setMember(member); //작성자의 member테이블 레코드 저장
            review.setContent(content);
            review.setStartPlace(startPlace);
            review.setEndPlace(endPlace);
            review.setDate(LocalDate.now());
            review.setRoute_point(routePoint); //(7/30)수정-route테이블은 삭제, 후기테이블에 routePoint 컬럼 추가.
            log.info("checklog: review.setRoutePoint(routePoint): {}", review.getRoute_point());

            reviewRepository.save(review);
            mapUrl = "html 동적 렌더링 구현 후 html 주소 저장 예정";

            reviewDetailDTO.setStartPlace(review.getStartPlace());
            reviewDetailDTO.setEndPlace(review.getEndPlace());
            reviewDetailDTO.setContent(review.getContent());
            reviewDetailDTO.setMapUrl(mapUrl);

        } else {
            mapUrl = "실패";
        }
        return reviewDetailDTO;
    }






/**상세조회 관련 메서드*/




    //컨트롤러에서 호출당하는메서드(경로데이터 빼올 때)
    public JsonDataEntity getJsonData(Long review_id) {
        log.info("checklog: ReviewService_getJsonData-review_id: {}", review_id);

        //review_id로 review레코드를 찾아서 route_id를 빼와야하고,
        // route_id로 다시 routePoint를 빼와야함.
        // routePoint는 JsonDataEntity구조와 매핑돼서 객체형태로 저장한 후 controller->html로 전달됨.
        try {
            log.info("checklog: ReviewService_getJsonData-review_id: {}", review_id);
            String routePointString = reviewRepository.routeData(review_id); //review_id로 route데이터 빼오는 과정
            log.info("checklog: ReviewService_getJsonData-routePoint: {}", routePointString);

            Map<String,Object> makeJson = new HashMap<>();
            makeJson.put("route_point", routePointString);

            ObjectMapper objectMapper = new ObjectMapper();
            routePointString = objectMapper.writeValueAsString(makeJson);

            //makeJson을 객체로 바꾸기.
            JsonDataEntity routePointList = objectMapper.readValue(routePointString,JsonDataEntity.class);

            log.info("checklog: ReviewService_getJsonData-routePointList: {}", routePointList);

            return routePointList; //일단 controller로 다시 반환 (html 동적 렌더링을 하기 위해서)

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //(데이터 빼올 때 )
    public ReviewDetailDTO reviewDetail(Long review_id, String mapUrl) {
        log.info("checklog: ReviewService_reviewDetail-review_id: {}", review_id);

        //review_id에 해당되는 저장값 가져오도록 repository 호출
        ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
        try {
            Optional<Review> reviewEntity = reviewRepository.detailReview(review_id);
            log.info("checklog: ReviewService_reviewDetail-reviewEntity: {}", reviewEntity);

            //startPlace,endPlace,content,date,member_id(nickname을 구하기 위해서 member_id도 포함)
            // Optional이기 때문에 null일 수도 있음.
            if (reviewEntity.isPresent()) {
                Review review = reviewEntity.get();
                String startPlace = review.getStartPlace();
                String endPlace = review.getEndPlace();
                String content = review.getContent();
                Long member_id = review.getMember().getMember_id();
                log.info("checklog: ReviewService_reviewDetail-startPlace: {}", startPlace);
                log.info("checklog: ReviewService_reviewDetail-endPlace: {}", endPlace);
                log.info("checklog: ReviewService_reviewDetail-content: {}", content);
                log.info("checklog: ReviewService_reviewDetail-member_id: {}", member_id);


/**
                //Optional이기 때문에 null일 수도 있음.
                Optional<Member> memberEntity = reviewRepository.detailMember(member_id);
                if (memberEntity.isPresent()) {
                    Member member = memberEntity.get();
                    String nickname = member.getNickname();
*/
                    //return할 dto로 변환
                    reviewDetailDTO.setStartPlace(startPlace);
                    reviewDetailDTO.setEndPlace(endPlace);
                    reviewDetailDTO.setContent(content);
                    reviewDetailDTO.setMapUrl(mapUrl);
                }

        } catch (Exception e) {

            return null; //예외처리 구현예정
        }
        return reviewDetailDTO;
    }
}

