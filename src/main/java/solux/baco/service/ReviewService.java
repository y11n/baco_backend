//origin/develop merge 테스트
package solux.baco.service;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solux.baco.service.ReviewModel.ReviewDTO;

@Service
@Slf4j
public class ReviewService {

    public void saveReview(@RequestHeader HttpSession session , @RequestBody ReviewDTO reviewData) {
        //수정된 기능)경로 좌표 반환 api 호출해서 경로좌표,(경로기준)출발좌표,(경로기준)도착좌표 받아오기=>저장할 데이터 준비 완료 => 기능 변경


        //2.트랜잭션으로 Route테이블과 Review테이블 묶어서 저장되도록 함.

        //3.응답 반환

    }

}
