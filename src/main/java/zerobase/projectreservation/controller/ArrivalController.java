package zerobase.projectreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.service.ArrivalService;

@RestController
@RequestMapping("/arrival")
@RequiredArgsConstructor
public class ArrivalController {

    private final ArrivalService arrivalService;

    /**
     *  키오스크
     *  예약한 유저의 도착정보 바꿈
     */
    @PostMapping
    public ResponseEntity<?> markArrival(@RequestParam String phoneNumber) {
        ReservationDto reservation = arrivalService.markArrivalByPhoneNumber(phoneNumber);
        String arrivalStatus = reservation.getArriveStatus().toString();
        return ResponseEntity.ok(arrivalStatus);
    }
}
