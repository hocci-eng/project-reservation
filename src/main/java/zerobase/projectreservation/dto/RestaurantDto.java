package zerobase.projectreservation.dto;

import lombok.Data;
import zerobase.projectreservation.domain.Restaurant;

@Data
public class RestaurantDto {

    private Double totalRating;
    private String name;
    private String description;

    public Restaurant toEntity() {
        return Restaurant.builder()
                .totalRating(0.0)
                .name(name)
                .description(description)
                .build();
    }
}
