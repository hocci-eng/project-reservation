package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Setter
    private Double rating;

    @Setter
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Setter
    @OneToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    public void addReview(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReviews().add(this);
    }

    public void removeReview() {
        if (this.restaurant != null) {
            this.restaurant.getReviews().remove(this);
            this.restaurant = null;
        }
    }

    /**
     * 리뷰 추가 시 평점 계산
     * (이전 평균 점수 * 이전 리뷰 갯수) + 현재 리뷰점수 / 현재 리뷰 갯수
     */
    public Double calcAddRating() {
        // 입력받은 리뷰의 레스토랑
        Restaurant restaurant = this.restaurant;

        // 레스토랑에 남긴 리뷰의 개수
        int newSize = restaurant.getReviews().size();

        return ((restaurant.getTotalRating() * (newSize - 1)) + this.getRating())
                / (newSize);
    }

    public Double calcUpdateRating(Double newRating) {
        Restaurant restaurant = this.restaurant;
        int size = restaurant.getReviews().size();

        Double oldRating = this.getRating();

        return ((restaurant.getTotalRating() * size) - oldRating + newRating) / size;
    }

    public Double calcRemoveRating() {
        Restaurant restaurant = this.restaurant;

        int size = restaurant.getReviews().size();

        return ((restaurant.getTotalRating() * size) - this.getRating()) / (size - 1);
    }


}
