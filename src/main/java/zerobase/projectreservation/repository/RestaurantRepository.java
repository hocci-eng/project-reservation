package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findRestaurantByName(String name);

    Optional<Restaurant> findRestaurantById(Long id);

    @Query("select distinct r from Restaurant r join fetch r.admin")
    List<Restaurant> findAllJoinFetch();

    @Query("select distinct r from Restaurant r join fetch r.admin order by r.name asc")
    List<Restaurant> getAllRestaurantsOrderByNameAsc();
}
