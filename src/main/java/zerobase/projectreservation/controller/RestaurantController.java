package zerobase.projectreservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.type.Partnership;
import zerobase.projectreservation.dto.RestaurantDto;
import zerobase.projectreservation.exception.impl.UnauthorizedCreationRestaurantException;
import zerobase.projectreservation.service.RestaurantService;

@Slf4j
@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * 매장 등록
     * 권한 -> admin
     * admin의 partner 권한이 PARTNER가 아니면 접근 불가
     */
    @PostMapping("/admin/add")
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantDto restaurantDto) {
        Admin admin = getAdmin();
        if (admin.getPartnership() != Partnership.PARTNER) {
            throw new UnauthorizedCreationRestaurantException();
        }

        Restaurant result = restaurantService.createRestaurant(
                restaurantDto, admin);

        return ResponseEntity.ok(result.toRestaurantDto());
    }

    /**
     * 매장 이름 조회
     * 권한 -> member
     */
    @GetMapping("/member/{restaurantName}")
    public ResponseEntity<?> getRestaurantForMember(@PathVariable String restaurantName) {
        Restaurant findRestaurant = restaurantService.getRestaurantByName(restaurantName);

        return ResponseEntity.ok(findRestaurant.toRestaurantDto());
    }

    /**
     * 등록된 매장 전체 조회
     * 권한 -> member
     */
    @GetMapping("/member/list")
    public ResponseEntity<?> getRestaurantsForMember(final Pageable pageable) {
        Page<RestaurantDto> result = restaurantService.getAllRestaurants(pageable);

        return ResponseEntity.ok(result);
    }

    /**
     * 등록된 가게 조회
     * 권한 -> admin
     */
    @GetMapping("/admin/list")
    public ResponseEntity<?> getRestaurantByAdminId(final Pageable pageable) {
        Page<RestaurantDto> result = restaurantService.getRestaurantsById(
                getAdmin(), pageable);

        return ResponseEntity.ok(result);
    }

    /**
     * 매장 정보 수정
     * 권한 -> admin
     */
    @PostMapping("/admin/{restaurantName}")
    public ResponseEntity<?> updateRestaurant(
            @RequestBody RestaurantDto newRestaurantDto,
            @PathVariable String restaurantName) {

        Restaurant result = restaurantService.updateRestaurant(
                getAdmin(), restaurantName, newRestaurantDto);

        return ResponseEntity.ok(result.toRestaurantDto());
    }

    /**
     * 매장 삭제
     * 권한 -> admin
     */
    @DeleteMapping("/admin/{restaurantName}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable String restaurantName) {
        String deletedRestaurantName = restaurantService.deleteRestaurant(
                getAdmin(), restaurantName);

        return ResponseEntity.ok(deletedRestaurantName);
    }

    /**
     * 인증된 사용자의 정보 가져오기
     */
    private static Admin getAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (Admin) authentication.getPrincipal();
    }
}
