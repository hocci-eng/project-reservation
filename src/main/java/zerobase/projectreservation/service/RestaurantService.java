package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.repository.AdminRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final AdminRepository adminRepository;

    /**
     * 레스토랑 등록
     * 등록된 레스토랑 이름 반환
     */
    public Restaurant createRestaurantInfo(Restaurant restaurant, String loginId) {
        Admin admin = adminRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("없는 회원입니다."));

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        savedRestaurant.addRestaurant(admin);
        return savedRestaurant;
    }

    /**
     * 매장 이름으로 조회 -> 유저용
     */
    public Restaurant getRestaurantByName(String name) {
        return restaurantRepository.findRestaurantByName(name)
                .orElseThrow(() -> new IllegalStateException("등록된 가게가 없습니다."));
    }

    /**
     * 매장 아이디로 조회 -> 내부에서 사용
     */
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findRestaurantById(id)
                .orElseThrow(() -> new IllegalStateException("등록된 가게가 없습니다."));
    }

    /**
     * 등록된 모든 가게 조회
     */
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> result = restaurantRepository.findAllJoinFetch();
        if (result.isEmpty()) {
            throw new IllegalStateException("등록된 가게가 없습니다.");
        }
        return result;
    }

    /**
     * 매장 정보 조회
     */
    public String getRestaurantDescription(Restaurant restaurant) {
        Restaurant result = restaurantRepository.findRestaurantByName(
                restaurant.getName()).orElseThrow(
                () -> new IllegalStateException("매장 설명이 없습니다."));

        return result.getDescription();
    }

    /**
     * 매장 정보 수정
     */
    public void updateRestaurantInfo(Long id, String newName, String newDescription) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id)
                .orElseThrow(
                        () -> new IllegalStateException("해당 매장이 없습니다."));

        restaurant.setName(newName);
        restaurant.setDescription(newDescription);
        restaurantRepository.save(restaurant);
    }

    /**
     * 매장 삭제
     */
    public void deleteRestaurant(Restaurant restaurant) {
        restaurant.removeRestaurant();
        restaurantRepository.delete(restaurant);
    }
}
