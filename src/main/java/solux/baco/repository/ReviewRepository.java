package solux.baco.repository;


import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Member;
import solux.baco.domain.Review;

@Repository
@Transactional
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //void에서 http응답 반환할 수 있도록 변환 예정
    public void save(Review review){
        /** review.setMember(writerInfo.get()); //작성자의 member테이블 레코드 저장
         review.setContent(content);
         review.setStartPlace(startPlace);
         review.setEndPlace(endPlace);
         */

    String sql = "INSERT INTO review (member,content,startPlace,endPlace) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, review.getMember(), review.getContent(), review.getStartPlace(),review.getEndPlace());

    }

    //getReviewByReview_id (게시글 상세조회)

    //getReviewByHashtag (특정 해시태그로 게시글 목록(게시판) 조회)
}
