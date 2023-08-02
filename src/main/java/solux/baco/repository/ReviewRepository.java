package solux.baco.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Member;
import solux.baco.domain.Review;
import solux.baco.domain.Route;
import solux.baco.service.RouteModel.JsonDataEntity;

import java.util.List;
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
    public Long save(Review review) {

        log.info("checklog: ReviewRepository_save-review.getEndPlace: {}", review.getEndPlace());
        log.info("checklog: ReviewRepository_save-review.getStartPlace: {}", review.getStartPlace());
        log.info("checklog: ReviewRepository_save-review.getContent: {}", review.getContent());
        log.info("checklog: ReviewRepository_save-review.getMember: {}", review.getMember());
        log.info("checklog: ReviewRepository_save-review.getRoutePoint: {}", review.getRoute_point());


        String sql = "INSERT INTO review (member_id,content,start_place,end_place, date,route_point) VALUES (?, ?, ?, ? , ?, ?)";
        jdbcTemplate.update(sql, review.getMember().getMember_id(), review.getContent(), review.getStartPlace(), review.getEndPlace(), review.getDate(), review.getRoute_point());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("review").usingGeneratedKeyColumns("review_id");

        Long review_id = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("member_id", review.getMember().getMember_id())
                .addValue("content",review.getContent())
                .addValue("start_place",review.getStartPlace())
                .addValue("end_place",review.getEndPlace())
                .addValue("date",review.getDate())
                .addValue("route_point",review.getRoute_point()))
                .longValue();

        return review_id;
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



    public Review findOne(Long reviewId) {
        return entityManager.find(Review.class, reviewId);
    }

    public List<Review> findMemberReviews(Long memberId) {
        return entityManager.createQuery("select m from Review m where m.member.member_id = :memberId", Review.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Review> findHashtagReviews(String hashtag) {
        return entityManager.createQuery("select m from Review m where m.hashtag = :hashtag", Review.class)
                .setParameter("hashtag", hashtag)
                .getResultList();
    }

}

