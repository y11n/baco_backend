package solux.baco.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Member;
import solux.baco.domain.Review;

import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //void에서 http응답 반환할 수 있도록 변환 예정
    public void save(Review review) {

        log.info("checklog: review.getEndPlace: {}", review.getEndPlace());

        log.info("checklog: review.getStartPlace: {}", review.getStartPlace());

        log.info("checklog: review.getContent: {}", review.getContent());

        log.info("checklog: review.getMember: {}", review.getMember());


        String sql = "INSERT INTO review (member_id,content,start_place,end_place, date) VALUES (?, ?, ?, ? , ?)";
        jdbcTemplate.update(sql, review.getMember().getMember_id(), review.getContent(), review.getStartPlace(), review.getEndPlace(), review.getDate());

    }
    //getReviewByReview_id (게시글 상세조회)_1.후기테이블정보
    public Optional<Review> detailReview(Long review_id) {
        Review review = entityManager.find(Review.class,review_id);
        return Optional.ofNullable(review);

    }

    //getReviewByReview_id (게시글 상세조회)_2.회원테이블정보
    public Optional<Member> detailMember(Long member_id) {
        Member member =  entityManager.find(Member.class,member_id);
        return Optional.ofNullable(member);
    }

    //getReviewByHashtag (특정 해시태그로 게시글 목록(게시판) 조회)
}
