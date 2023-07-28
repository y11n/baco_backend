package solux.baco.repository;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Member;
import solux.baco.domain.Review;

@Slf4j
@Repository
@Transactional
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //void에서 http응답 반환할 수 있도록 변환 예정
    public void save(Review review){

        log.info("checklog: review.getEndPlace: {}",review.getEndPlace());

        log.info("checklog: review.getStartPlace: {}",review.getStartPlace());

        log.info("checklog: review.getContent: {}",review.getContent());

        log.info("checklog: review.getMember: {}",review.getMember());



    String sql = "INSERT INTO review (member_id,content,start_place,end_place) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, review.getMember().getMember_id(), review.getContent(), review.getStartPlace(),review.getEndPlace());

    }

    //getReviewByReview_id (게시글 상세조회)

    //getReviewByHashtag (특정 해시태그로 게시글 목록(게시판) 조회)
}
