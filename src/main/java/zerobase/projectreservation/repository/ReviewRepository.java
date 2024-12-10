package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewById(Long id);

    @Query("select rs.review from Reservation rs where rs.id = :reservationId ")
    Optional<Review> findReviewByReservationId(
            @Param("reservationId") Long reservationId
    );

    @Query("select re from Review re " +
            "join fetch re.reservation r " +
            "join fetch r.member m where r.member.id = :memberId")
    Optional<Review> findReviewByMemberId(@Param("memberId") Long id);
}
