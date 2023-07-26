package solux.baco.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
=======
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
import solux.baco.service.RouteService;
>>>>>>> develop

@Slf4j
@RestController
@RequestMapping("/Review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    //후기 저장(후기작성)
    @PostMapping("/create")
    public ResponseEntity<String> saveReview(@RequestParam double[] start, @RequestParam double[] end, @RequestBody ReviewDTO reviewData) { //요청바디와 데이터 매핑.
        try {
            //전달받은 데이터 예외처리


            //정상적인 로직 가능한 경우에 실행되는 부분
            //ReviewService 호출
            reviewService.saveReview(start,end,reviewData);

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


<<<<<<< HEAD
}
=======
}
>>>>>>> develop
