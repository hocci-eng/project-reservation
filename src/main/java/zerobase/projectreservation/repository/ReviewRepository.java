package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewById(Long id);

    @Query("select r from Review r join fetch r.restaurant")
    List<Review> findReviewsByRestaurantId(Long id);

    // reservation id로 review 찾기
    @Query("select rs.review from Reservation rs where rs.id = :reservationId ")
    Optional<Review> findReviewByReservationId(
            @Param("reservationId") Long reservationId
    );

    void deleteReviewById(Long id);
}
