package solux.baco.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import solux.baco.domain.Review;

import java.util.List;

@Repository
public class ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    public Review findOne(Long reviewId){
        return em.find(Review.class, reviewId);
    }

    public List<Review> findMemberReviews(Long memberId) {
        return em.createQuery("select m from Review m where m.member.member_id = :memberId", Review.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Review> findHashtagReviews(String hashtag){
        return em.createQuery("select m from Review m where m.hashtag = :hashtag", Review.class)
                .setParameter("hashtag", hashtag)
                .getResultList();
    }
}
