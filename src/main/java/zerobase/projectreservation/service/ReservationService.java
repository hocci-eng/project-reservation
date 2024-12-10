package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.exception.impl.AlreadyExistReservationException;
import zerobase.projectreservation.exception.impl.NotFoundReservationException;
import zerobase.projectreservation.exception.impl.NotFoundRestaurantException;
import zerobase.projectreservation.repository.ReservationRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * 매장 예약 생성
     */
    public Reservation createReservation(
            String restaurantName, ReservationDto reservationDto, Member member) {

        Restaurant restaurant = restaurantRepository.findRestaurantByName(restaurantName)
                .orElseThrow(NotFoundRestaurantException::new);

        boolean exists = isExistsReservation(reservationDto, restaurant);

        if (exists) {
            throw new AlreadyExistReservationException();
        }

        Reservation reservation = reservationDto.toEntity(member, restaurant);
        reservation.addReservation(restaurant);

        return reservationRepository.save(reservation);
    }

    /**
     *  예약 가능한 시간대인지 확인
     */
    private boolean isExistsReservation(ReservationDto reservationDto, Restaurant restaurant) {
        return reservationRepository.existsByRestaurantIdAndDateAndTime(
                restaurant.getId(), reservationDto.getDate(), reservationDto.getTime());
    }

    /**
     * 예약 조회
     */
    @Transactional(readOnly = true)
    public ReservationDto getReservation(Member member) {
        Reservation reservation = reservationRepository.findReservationByMemberById(
                member.getId())
                .orElseThrow(NotFoundReservationException::new);
        return reservation.toReservationDto();
    }

    /**
     * 예약 삭제
     */
    public String deleteReservation(Member member) {
        Reservation reservation = reservationRepository.findReservationByMemberById(
                member.getId())
                .orElseThrow(NotFoundReservationException::new);

        reservationRepository.delete(reservation);
        return reservation.getRestaurant().getName();
    }
}
