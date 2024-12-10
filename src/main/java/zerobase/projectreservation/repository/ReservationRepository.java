package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r join fetch r.member m " +
            "where r.member.id = :memberId")
    Optional<Reservation> findReservationByMemberById(@Param("memberId") Long id);

    boolean existsByRestaurantIdAndDateAndTime(
            Long restaurantId, LocalDate date, LocalTime time);
}
