package zerobase.projectreservation.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.dto.AdminAuth;
import zerobase.projectreservation.dto.RestaurantDto;
import zerobase.projectreservation.repository.AdminRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    EntityManager em;

    @Test
    void 레스토랑_등록() {
        // given
        Admin admin = adminService.register(
                createAdmin(
                        "사업자번호", "password", "jay",
                        "01011111111")
        );

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("매장1");
        restaurantDto.setDescription("고기집입니다.");

        Restaurant restaurant = restaurantService.createRestaurantInfo(
                restaurantDto.toEntity(), admin.getLoginId());

        // when

        // then
        assertEquals(restaurant.getAdmin(), admin);
        assertEquals(restaurant.getName(), restaurantDto.getName());
    }

    @Test
    void 매장_이름_조회() {
        // given
        Restaurant entity = createRestaurantDto(
                "매장1", "매장설명").toEntity();

        Admin admin = adminService.register(
                createAdmin(
                        "사업자번호", "password", "jay",
                        "01011111111")
        );

        Restaurant savedRestaurant = restaurantService.createRestaurantInfo(
                entity, admin.getLoginId());

        // when
        Restaurant findRestaurant = restaurantService.getRestaurantByName(
                savedRestaurant.getName());


        // then
        assertEquals(savedRestaurant.getName(), findRestaurant.getName());
    }

    @Test
    void 등록된_모든_가게_조회() {
        // given

        List<Admin> admins = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            admins.add(createAdmin(
                    "user" + i, "password" + i, "유저" + i,
                    "0101111111" + i).toEntity()
            );
        }
        adminRepository.saveAll(admins);

        List<Restaurant> entities = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            entities.add(
                    createRestaurantDto("매장" + i, "매장설명" + i)
                            .toEntity()
            );
        }

        restaurantRepository.saveAll(entities);

        // when
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).addRestaurant(admins.get(i));
        }
        em.flush();
        em.clear();

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        // then
        for (int i = 0; i < restaurants.size(); i++) {
            assertEquals(restaurants.get(i).getName(), entities.get(i).getName());
        }
    }

    @Test
    void 매장_정보_조회() {
        // given
        RestaurantDto restaurantDto = createRestaurantDto("매장1", "매장설명1");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        // when
        String description = restaurantService.getRestaurantDescription(restaurant);

        // then
        assertEquals(description, restaurantDto.getDescription());
    }

    @Test
    void 회원_매장_조회_nPlus1_문제_검증() {
        // given
        // admin 10명 생성
        List<Admin> admins = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            admins.add(createAdmin(
                    "user" + i, "password" + i, "유저" + i,
                    "0101111111" + i).toEntity()
            );
        }

        adminRepository.saveAll(admins);

        // 가게 10개 생성
        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            RestaurantDto restaurantDto = new RestaurantDto();
            restaurantDto.setName("가게" + i);
            restaurantDto.setDescription("가게설명" + i);
            restaurants.add(restaurantDto.toEntity());
        }

        restaurantRepository.saveAll(restaurants);

        System.out.println("== 구분선3 ==");
        for (int i = 0; i < restaurants.size(); i++) {
            restaurants.get(i).addRestaurant(admins.get(i));
        }

        // when
        em.flush();
        em.clear();

        // then
        System.out.println("== 구분선4 ==");
        // List<Admin> result = adminRepository.findAll(); // N+1 발생

        List<Admin> result = adminRepository.findAllJoinFetch();
        List<String> restaurantNames = result.stream().flatMap(
                it -> it.getRestaurants().stream().map(Restaurant::getName
                )).toList();

        for (String restaurantName : restaurantNames) {
            System.out.println("restaurantName = " + restaurantName);
        }
        System.out.println("== 구분선4 ==");
    }

    @Test
    void 레스토랑_수정() {
        // given
        Admin admin = adminService.register(
                createAdmin(
                        "사업자번호", "password", "jay",
                        "01011111111")
        );

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("매장1");
        restaurantDto.setDescription("고기집입니다.");

        Restaurant restaurant = restaurantService.createRestaurantInfo(
                restaurantDto.toEntity(), admin.getLoginId());

        String newName = "새로운 매장";
        String newDescription = "새로운 매장 설명";

        // when
        restaurantService.updateRestaurantInfo(restaurant.getId(), newName, newDescription);

        // then
        Assertions.assertThat(restaurant.getName()).isEqualTo(newName);
        Assertions.assertThat(restaurant.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void 레스토랑_삭제() {
        // given
        Admin admin = adminService.register(
                createAdmin(
                        "사업자번호", "password", "jay",
                        "01011111111")
        );

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("매장1");
        restaurantDto.setDescription("고기집입니다.");

        Restaurant restaurant = restaurantService.createRestaurantInfo(
                restaurantDto.toEntity(), admin.getLoginId());

        // when
        restaurantService.deleteRestaurant(restaurant);

        // then
        assertEquals(restaurant.getAdmin(), null);
        assertEquals(admin.getRestaurants().isEmpty(), true);
    }

    private static AdminAuth.SignUp createAdmin(String loginId, String password,
                                                String username, String phoneNumber) {
        AdminAuth.SignUp adminAuth = new AdminAuth.SignUp();
        adminAuth.setLoginId(loginId);
        adminAuth.setPassword(password);
        adminAuth.setUsername(username);
        adminAuth.setPhoneNumber(phoneNumber);
        return adminAuth;
    }

    private static RestaurantDto createRestaurantDto(String name, String description) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(name);
        restaurantDto.setDescription(description);
        return restaurantDto;
    }
}