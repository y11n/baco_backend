//origin/develop merge 테스트
package solux.baco.service;


import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.repository.ReviewRepository;
import solux.baco.service.ReviewModel.ReviewDetailDTO;

import java.time.LocalDate;
import java.util.List;
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
    public ReviewDetailDTO saveReview(String startPlace, String endPlace, String content, String routePoint) {
        log.info("checklog: review.setRoutePoint(routePoint): {}", startPlace);
        log.info("checklog: review.setRoutePoint(routePoint): {}", endPlace);
        log.info("checklog: review.setRoutePoint(routePoint): {}", content);
        log.info("checklog: review.setRoutePoint(routePoint): {}", routePoint);


        /**
         //수정된 기능) 서울 내 특정 장소명을 string으로 전달받으면, 데이터가 있는 경우에 한해서 좌표값으로 경로api 호출 후, 경로 저장.
         //이후, 게시글 상세보기 요청이 들어오면 경로데이터를 html에 렌더링해서 html에는 경로가 나타날 거고, 그 html의 url을 프론트엔드로 string형태로 넘긴 후
         //프론트엔드에서는 iframe src를 해당 url로 연결하면 끝!!!
         */

        Review review = new Review();
        ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
        String mapUrl;
        String email ="test2@test.com";

/**
 //memberService의 findEmail메서드를 호출하고,
 // memberService의 findEmail 메서드는 memberRepository의 findByEmail을 호출하고,
 // memberRepository의 findByEmail메서드는 Member객체를 반환함. (Optional<Member>)
 //Member객체에서 get으로 member_id를 찾으면 됨.
 */

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
        //반활할 값만 얻기 위해서 따로 객체 생성?

        return reviewDetailDTO;
    }


    /**
     * 경로 json 테스트
     */
    /**
     //컨트롤러에서 호출당하는메서드(경로데이터 빼올 때)
     public JsonDataEntity getJsonData(Long review_id) {
     //review_id로 review레코드를 찾아서 route_id를 빼와야하고,
     // route_id로 다시 routePoint를 빼와야함.
     // routePoint는 JsonDataEntity구조와 매핑돼서 객체형태로 저장한 후 controller->html로 전달됨.
     try {
     String routePoint = reviewRepository.routeData(review_id); //review_id로 route데이터 빼오는 과정

     //routePoint를 jsonDataEntity형태로 파싱하기.
     ObjectMapper objectMapper = new ObjectMapper();

     JsonDataEntity jsonDataEntity = objectMapper.readValue(routePoint, JsonDataEntity.class);
     return jsonDataEntity;


     } catch (IOException e) {
     e.printStackTrace();
     return null;
     }
     }

     /**
     * 경로 json 테스트
     */
    /**
     * //(데이터 빼올 때 )
     * public ReviewDetailDTO reviewDetail(Long review_id, String mapUrl) {
     * //review_id에 해당되는 저장값 가져오도록 repository 호출
     * ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
     * try {
     * Optional<Review> reviewEntity = reviewRepository.detailReview(review_id);
     * //startPlace,endPlace,content,date,member_id(nickname을 구하기 위해서 member_id도 포함)
     * //Optional이기 때문에 null일 수도 있음.
     * if (reviewEntity.isPresent()) {
     * Review review = reviewEntity.get();
     * String startPlace = review.getStartPlace();
     * String endPlace = review.getEndPlace();
     * String content = review.getContent();
     * LocalDate date = review.getDate();
     * Long member_id = review.getMember().getMember_id();
     * <p>
     * //Optional이기 때문에 null일 수도 있음.
     * Optional<Member> memberEntity = reviewRepository.detailMember(member_id);
     * if (memberEntity.isPresent()) {
     * Member member = memberEntity.get();
     * String nickname = member.getNickname();
     * <p>
     * <p>
     * //return할 dto로 변환
     * reviewDetailDTO.setStartPlace(startPlace);
     * reviewDetailDTO.setEndPlace(endPlace);
     * reviewDetailDTO.setContent(content);
     * reviewDetailDTO.setDate(date);
     * reviewDetailDTO.setNickname(nickname);
     * reviewDetailDTO.setMapUrl(mapUrl);
     * }
     * <p>
     * <p>
     * }
     * } catch (Exception e) {
     * <p>
     * return null; //예외처리 구현예정
     * }
     * return reviewDetailDTO;
     * }
     * <p>
     * /**  String startPlace = reviewData.getStartPlace();
     * String endPlace = reviewData.getEndPlace();
     * String content = reviewData.getContent();
     */


}


