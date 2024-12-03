package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private Double score;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void addReview(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReviews().add(this);
    }
}
