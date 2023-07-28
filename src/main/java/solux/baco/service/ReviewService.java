//origin/develop merge 테스트
package solux.baco.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.repository.ReviewRepository;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.RouteModel.RouteDTO;

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

    public void saveReview(String email, String startPlace,  String endPlace,  String content ) { //email대신 HttpSession session
        //수정된 기능)경로 좌표 반환 api 호출해서 경로좌표,(경로기준)출발좌표,(경로기준)도착좌표 받아오기=>저장할 데이터 준비 완료 => 기능 변경
/**세션부분은 테스트 생략
        //1. 세션에서 이메일 추출하기
        String email = (String) session.getAttribute("loginEmail");
        log.info("checklog: loginEmail : {}", email);
        //전달받은 데이터 예외처리


 //memberService의 findEmail메서드를 호출하고,
 // memberService의 findEmail 메서드는 memberRepository의 findByEmail을 호출하고,
 // memberRepository의 findByEmail메서드는 Member객체를 반환함. (Optional<Member>)
 //Member객체에서 get으로 member_id를 찾으면 됨.
 */

        log.info("checklog: ReviewService");
        //2. 이메일을 통해서 작성자의 Member객체 받아오기
        Optional<Member> writerInfo = memberService.findByEmail(email);
        log.info("checklog: writerInfo: {}",writerInfo);

        //3. 받아온 Member객체를 통해서 member_id 추출하기
        if (writerInfo.isPresent()) {
            //Optional 객체 속의 요소인 Member객체를 가져오기 위해 .get() //(null이 아닐 때만 get으로 가져올 수 있음.)
            Member member = writerInfo.get();
            //Long member_id = member.getMember_id();

            Review review = new Review();
            review.setMember(member); //작성자의 member테이블 레코드 저장
            review.setContent(content);
            review.setStartPlace(startPlace);
            review.setEndPlace(endPlace);
            log.info("checklog: review: {}",review);
            reviewRepository.save(review);

            }





        }

        //응답 반환


    }


