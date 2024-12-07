package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * 매장 예약 생성
     * @Param 외부에서 예약할 매장정보를 받아옴
     */
    public Reservation createReservation(ReservationDto reservationDto,
                                         Member member, Restaurant restaurant) {

        Reservation reservation = reservationDto.toEntity(member, restaurant);
        reservation.addReservation(restaurant);

        return reservationRepository.save(reservation);
    }

    /**
     * 예약 조회
     */
}
