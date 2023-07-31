package solux.baco.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.domain.Route;
import solux.baco.service.RouteModel.JsonDataEntity;

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



    /**후기 저장 메서드*/
    //저장 결과를 다시 클라이언트 측에 나타내기 위해서 다시 반환.
    public Optional<Review> save(Review review) {

        log.info("checklog: review.getEndPlace: {}", review.getEndPlace());

        log.info("checklog: review.getStartPlace: {}", review.getStartPlace());

        log.info("checklog: review.getContent: {}", review.getContent());

        log.info("checklog: review.getMember: {}", review.getMember());

        log.info("checklog: review.getRoutePoint: {}", review.getRoute_point());


        String sql = "INSERT INTO review (member_id,content,start_place,end_place, date,route_point) VALUES (?, ?, ?, ? , ?, ?)";
        jdbcTemplate.update(sql, review.getMember().getMember_id(), review.getContent(), review.getStartPlace(), review.getEndPlace(), review.getDate(), review.getRoute_point());

        return Optional.ofNullable(review);
    }





    /**r후기 상세 조회 관련 메서드*/
    public String routeData(Long review_id) {
        Review review = entityManager.find(Review.class,review_id);
        String routeData = review.getRoute_point();
        return routeData;
    }


    //getReviewByReview_id (게시글 상세조회)_1.후기테이블정보
    public Optional<Review> detailReview(Long review_id) {
        Review review = entityManager.find(Review.class, review_id);
        return Optional.ofNullable(review);

    }

    //getReviewByReview_id (게시글 상세조회)_2.회원테이블정보
    public Optional<Member> detailMember(Long member_id) {
        Member member = entityManager.find(Member.class, member_id);
        return Optional.ofNullable(member);
    }

}
