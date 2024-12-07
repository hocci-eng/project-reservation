package zerobase.projectreservation.dto;

import lombok.Data;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.Review;

@Data
public class ReviewDto {

    private Double rating;
    private String comment;
    private Restaurant restaurant;

    public Review toEntity(Restaurant restaurant) {
        return Review.builder()
                .rating(rating)
                .comment(comment)
                .restaurant(restaurant)
                .build();
    }
}