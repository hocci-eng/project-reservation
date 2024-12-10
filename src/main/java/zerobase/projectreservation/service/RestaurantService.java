package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.dto.RestaurantDto;
import zerobase.projectreservation.exception.impl.NotFoundRestaurantException;
import zerobase.projectreservation.exception.impl.NotRegisteredException;
import zerobase.projectreservation.repository.AdminRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AdminRepository adminRepository;

    /**
     * 레스토랑 등록
     * 등록된 레스토랑 이름 반환
     */
    public Restaurant createRestaurant(RestaurantDto restaurantDto, Admin admin) {
        Admin findAdmin = adminRepository.findByLoginId(admin.getLoginId())
                .orElseThrow(NotRegisteredException::new);

        Restaurant restaurant = restaurantDto.toEntity();
        restaurant.addRestaurant(findAdmin);

        return restaurantRepository.save(restaurant);
    }

    /**
     * 매장 이름으로 조회
     * member 사용
     */
    @Transactional(readOnly = true)
    public Restaurant getRestaurantByName(String restaurantName) {

        return restaurantRepository.findRestaurantByName(restaurantName)
                .orElseThrow(NotFoundRestaurantException::new);
    }

    /**
     * 등록된 모든 가게 이름순으로 정렬 조회용
     * member 사용
     */
    @Transactional(readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(Pageable pageable) {
        Page<RestaurantDto> result = restaurantRepository.getAllRestaurantsOrderByName(pageable)
                .map(Restaurant::toRestaurantDto);
        if (result.isEmpty()) {
            throw new NotFoundRestaurantException();
        }
        return result;
    }

    /**
     * 등록된 가게 조회
     * admin 사용
     */
    @Transactional(readOnly = true)
    public Page<RestaurantDto> getRestaurantsById(Admin admin, Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository
                .findRestaurantsByAdminId(admin.getId(), pageable);
        if (restaurants.isEmpty()) {
            throw new NotFoundRestaurantException();
        }

        return restaurants.map(Restaurant::toRestaurantDto);
    }

    /**
     * 매장 정보 수정
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Restaurant updateRestaurant(
            Admin admin, String restaurantName, RestaurantDto newRestaurantDto) {

        Restaurant restaurant = restaurantRepository.findRestaurantByAdminIdAndName(
                        admin.getId(), restaurantName)
                .orElseThrow(NotFoundRestaurantException::new);

        restaurant.setName(newRestaurantDto.getName());
        restaurant.setName(newRestaurantDto.getName());

        return restaurant;
    }

    /**
     * 매장 삭제
     */
    public String deleteRestaurant(Admin admin, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findRestaurantByAdminIdAndName(
                        admin.getId(), restaurantName)
                .orElseThrow(NotFoundRestaurantException::new);

        restaurant.removeRestaurant();
        restaurantRepository.delete(restaurant);
        return restaurant.getName();
    }
}
