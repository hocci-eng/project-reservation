package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;
import zerobase.projectreservation.dto.ReviewDto;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseEntity {

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
    @OneToOne(fetch = FetchType.LAZY)
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
     * (이전 평균 점수 * 이전 리뷰 갯수) + 현재 리뷰 점수 / 현재 리뷰 개수
     */
    public Double calcAddRating() {
        // 입력받은 리뷰의 레스토랑
        Restaurant restaurant = this.restaurant;

        // 레스토랑에 남긴 리뷰의 개수
        int newSize = restaurant.getReviews().size();

        return ((restaurant.getTotalRating() * (newSize - 1)) + this.getRating())
                / (newSize);
    }

    /**
     * 리뷰 업데이트 시 평점 계산
     * ((현재 평균 리뷰 점수 * 현재 리뷰 개수) - 이전 작성한 평점 + 새로 작성한 평점)) / 리뷰 전체 개수
     */
    public Double calcUpdateRating(Double newRating) {
        Restaurant restaurant = this.restaurant;
        int size = restaurant.getReviews().size();

        Double oldRating = this.getRating();

        return ((restaurant.getTotalRating() * size) - oldRating + newRating) / size;
    }

    /**
     * 리뷰 삭제 시 평점 계산
     * 현재 리뷰가 1개 남아있을 때 삭제하면 리뷰가 0개이므로 return 0.0
     * ((현재 평균 리뷰 점수 * 현재 리뷰 개수) - 삭제할 리뷰 점수) / 삭제한 후의 전체 리뷰 개수
     */
    public Double calcRemoveRating() {
        Restaurant restaurant = this.restaurant;

        int size = restaurant.getReviews().size();
        if (size <= 1) {
            return 0.0;
        }
        return ((restaurant.getTotalRating() * size) - this.getRating()) / (size - 1);
    }

    public ReviewDto toReviewDto() {
        return ReviewDto.builder()
                .rating(rating)
                .comment(comment)
                .build();
    }
}
