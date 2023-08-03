package solux.baco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solux.baco.domain.Review;
import solux.baco.service.ReviewModel.ReviewListDTO;
import solux.baco.service.ReviewService;

import java.util.List;

@Controller
@RestController
@RequestMapping("/Review")
public class ReviewBoardController {
    private final ReviewService reviewService;

    public ReviewBoardController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/reviewBoard")
    public List<ReviewListDTO> reviewBoardController(){
        return reviewService.allReviews();
    }
}