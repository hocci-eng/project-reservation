package zerobase.projectreservation.dto;

import lombok.Data;
import zerobase.projectreservation.domain.Restaurant;

@Data
public class RestaurantDto {

    private String name;
    private String description;

    public Restaurant toEntity() {
        return Restaurant.builder()
                .name(name)
                .description(description)
                .build();
    }
}
