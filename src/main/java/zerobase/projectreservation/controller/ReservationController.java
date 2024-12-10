package zerobase.projectreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.service.ReservationService;

@RestController
@RequestMapping("reservation/member/")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 생성
     */
    @PostMapping("/{restaurantName}")
    public ResponseEntity<?> createReservation(
            @PathVariable("restaurantName") String restaurantName,
            @RequestBody ReservationDto reservationDto) {

        Reservation reservation = reservationService.createReservation(
                restaurantName, reservationDto, getMember());

        return ResponseEntity.ok(reservation.toReservationDto());
    }

    /**
     * 예약 조회
     */
    @GetMapping
    public ResponseEntity<?> getReservation() {
        ReservationDto reservation = reservationService.getReservation(getMember());
        return ResponseEntity.ok(reservation);
    }

    /**
     * 예약 삭제
     */
    @DeleteMapping
    public ResponseEntity<?> deleteReservation() {
        String reservation = reservationService.deleteReservation(getMember());
        return ResponseEntity.ok(reservation);
    }

    /**
     * 인증된 사용자의 정보 가져오기
     */
    private static Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (Member) authentication.getPrincipal();
    }
}
