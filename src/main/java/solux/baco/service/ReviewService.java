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

    public void saveReview(String email , ReviewDTO reviewData) {
        //수정된 기능)경로 좌표 반환 api 호출해서 경로좌표,(경로기준)출발좌표,(경로기준)도착좌표 받아오기=>저장할 데이터 준비 완료 => 기능 변경


        //email로 member_id받아오기

        //date(작성일) 생성하기

        //reviewData에서 startPlace,endPlace,content 추출하기

        //트랜잭션으로 진행할 부분
        //후기테이블에 저장(member_id,date, startPlace,endPlace,content)
        //review_id생성
        //트랜잭션 끝

        //응답 반환

    }

}
