package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    @Column(name = "restaurant_rating")
    private Double totalRating;

    @Setter
    @Column(name = "restaurant_name")
    private String name;
    @Setter
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Reservation> reservations = new ArrayList<>();

    public void addRestaurant(Admin admin) {
        this.admin = admin;
        admin.getRestaurants().add(this);
    }

    public void removeRestaurant() {
        if (this.admin != null) {
            this.admin.getRestaurants().remove(this);
            this.admin = null;
        }
    }
}
