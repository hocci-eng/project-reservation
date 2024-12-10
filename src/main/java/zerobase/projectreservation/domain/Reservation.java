package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;
import zerobase.projectreservation.domain.type.ArriveStatus;
import zerobase.projectreservation.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"date", "time"}),
                @UniqueConstraint(columnNames = {"member_id", "reservation_id"})
        }
)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;

    @Setter
    @Enumerated(EnumType.STRING)
    private ArriveStatus arriveStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    public void addReservation(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReservations().add(this);
    }

    public ReservationDto toReservationDto() {
        return ReservationDto.builder()
                .date(date)
                .time(time)
                .member(member.toMemberDto().toEntity())
                .restaurant(restaurant.toRestaurantDto().toEntity())
                .arriveStatus(arriveStatus)
                .build();
    }
}
