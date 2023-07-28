package solux.baco.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solux.baco.domain.Member;
import solux.baco.service.MemberService;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
import solux.baco.service.RouteService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/Review")

public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @Autowired
    public ReviewController(ReviewService reviewService, MemberService memberService) {
        this.reviewService = reviewService;
        this.memberService = memberService;
    }




    //후기 저장(후기작성)
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<String> writeReviewController(@RequestHeader HttpSession session , @RequestBody ReviewDTO reviewData) { //요청바디와 데이터 매핑.
        try {

            //세션 받아오면 테스트가능한 부분인지?
            String email = (String) session.getAttribute("loginEmail");
            log.info("checklog: loginEmail : {}",email);
            //전달받은 데이터 예외처리


            //세션에서 이메일 추출하기
            Optional<Member> writerInfo = memberService.findByEmail(email);
            //log.info("checkLog:ReviewController - writeReviewController called with wirter_id: {}",writerInfo);

            if (writerInfo.isPresent()) {
                //Optional 객체 속의 요소인 Member객체를 가져오기 위해 .get() //(null이 아닐 때만 get으로 가져올 수 있음.)
                Member member = writerInfo.get();
                Long member_id = member.getMember_id();
                log.info("checkLog: ReviewController - writeReviewController called with member_id: {}", member_id);
            } else {
                //예외처리
                log.info("checkLog: ReviewController - Member not found with email: {}", email);
            }




            /**정상적인 로직 가능한 경우에 실행되는 부분*/

            //ReviewService 호출 => 기능 변경
            //reviewService에서 email=>member_id찾기,작성일은 어디서 추가?
            //ReviewDTO를 Service로 전달? 아니면 Controller에서 일단 요소를 추출?
            reviewService.saveReview(email,reviewData);

            //세션을 통해서 회원 정보 조회
            //세션에서 loginEmail이라는 속성을 통해 얻은 정보를 string으로 변환해서 loginEmail이라는 string 변수에 담아줌.
            String loginEmail = (String) session.getAttribute("loginEmail");
            //얻은 loginEmail로 회원고유식별자를 알아냄. => MemberService의 기능을 사용하려면 생성자 만들어야할 것같음.

            //memberService의 findEmail메서드를 호출하고,
            // memberService의 findEmail 메서드는 memberRepository의 findByEmail을 호출하고,
            // memberRepository의 findByEmail메서드는 Member객체를 반환함. (Optional<Member>)


            Optional<Member> writerOfMember = memberService.findByEmail(loginEmail);
            log.info("checkLog:ReviewController - writeReviewController called with wirter_id: {}",writerOfMember);



            //db저장 하는 service호출(이후 repository 호출)


            //db저장 성공 시
            return ResponseEntity.status(HttpStatus.CREATED).body("후기가 저장됐습니다.");


        } catch (Exception e) {
            //db저장 실패 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("후기 저장에 실패하였습니다.");

        }


    }


    //후기 게시판 목록 조회

    //후기 게시글 상세 조회

    //작성글 목록 조회

    //작성글 조회


}
