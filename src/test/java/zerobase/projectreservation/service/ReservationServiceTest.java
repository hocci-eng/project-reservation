package zerobase.projectreservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberAuth;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.dto.RestaurantDto;
import zerobase.projectreservation.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired ReservationService reservationService;

    @Autowired RestaurantService restaurantService;

    @Autowired RestaurantRepository restaurantRepository;

    @Autowired MemberService memberService;

    @Test
    void 예약_생성() {
        // given
        MemberAuth.SignUp memberDto = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );

        Member member = memberService.register(memberDto);

        RestaurantDto restaurantDto = getRestaurant("스타벅스", "카페입니다.");
        Restaurant savedRestaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDate(LocalDate.now());
        reservationDto.setTime(LocalTime.now());


        // when
        Restaurant findRestaurant = restaurantService.getRestaurantById(savedRestaurant.getId());

        Reservation reservation = reservationService.createReservation(reservationDto, member, findRestaurant);

        // then
        assertEquals(reservation.getMember(), member);
        assertEquals(reservation.getRestaurant(), findRestaurant);
    }

    private static RestaurantDto getRestaurant(String name, String description) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(name);
        restaurantDto.setDescription(description);
        return restaurantDto;
    }

    private static MemberAuth.SignUp createMember(String loginId, String password, String username, String phoneNumber, Authority authority) {
        MemberAuth.SignUp memberAuth = new MemberAuth.SignUp();
        memberAuth.setLoginId(loginId);
        memberAuth.setPassword(password);
        memberAuth.setUsername(username);
        memberAuth.setPhoneNumber(phoneNumber);
        memberAuth.setAuthority(authority);
        return memberAuth;
    }
}