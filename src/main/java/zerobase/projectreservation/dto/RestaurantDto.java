package zerobase.projectreservation.dto;

import lombok.Builder;
import lombok.Data;
import zerobase.projectreservation.domain.Restaurant;

@Data
@Builder
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
