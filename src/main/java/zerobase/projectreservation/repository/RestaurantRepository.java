package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // 유저 매장 검색용
    Optional<Restaurant> findRestaurantByName(String name);

    // 비즈니스 로직에서 사용할 용
    Optional<Restaurant> findRestaurantById(Long id);

    @Query("select distinct r from Restaurant r join fetch r.admin")
    List<Restaurant> findAllJoinFetch();
}
