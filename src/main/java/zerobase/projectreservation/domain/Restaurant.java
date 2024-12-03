package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Restaurant {

    @Id @GeneratedValue
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "restaurant_name")
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "restaurant")
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private final List<Reservation> reservations = new ArrayList<>();

    public void addRestaurant(Admin admin) {
        this.admin = admin;
        admin.getRestaurants().add(this);
    }
}
