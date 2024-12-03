package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.projectreservation.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
