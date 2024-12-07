package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;
import zerobase.projectreservation.domain.type.ArriveStatus;

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
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private ArriveStatus arriveStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter(AccessLevel.PUBLIC)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(mappedBy = "reservation")
    private Review review;

    public void addReservation(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReservations().add(this);
    }

}
