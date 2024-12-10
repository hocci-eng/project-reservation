package zerobase.projectreservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Restaurant;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findRestaurantByName(String name);

    @Query("select r from Restaurant r where r.admin.id = :id and r.name = :name")
    Optional<Restaurant> findRestaurantByAdminIdAndName(
            @Param("id") Long id, @Param("name") String name
    );

    @Query("select r from Restaurant r where r.admin.id = :id")
    Page<Restaurant> findRestaurantsByAdminId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT r FROM Restaurant r ORDER BY r.name")
    Page<Restaurant> getAllRestaurantsOrderByName(Pageable pageable);
}
