package zerobase.projectreservation.dto;

import lombok.Builder;
import lombok.Data;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.type.ArriveStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReservationDto {

    private LocalDate date;
    private LocalTime time;
    private Member member;
    private Restaurant restaurant;
    private ArriveStatus arriveStatus;

    public Reservation toEntity(Member member, Restaurant restaurant) {
        return Reservation.builder()
                .date(date)
                .time(time)
                .member(member)
                .restaurant(restaurant)
                .arriveStatus(ArriveStatus.NOT_ARRIVED)
                .build();
    }
}
