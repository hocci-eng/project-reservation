package zerobase.projectreservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.Review;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.*;
import zerobase.projectreservation.repository.MemberRepository;
import zerobase.projectreservation.repository.ReservationRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void 리뷰_생성() {
        // given
        // 멤버 생성
        MemberDto.SignUp memberAuth = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );

        Member member = memberRepository.save(memberAuth.toEntity());

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDate(LocalDate.now());
        reservationDto.setTime(LocalTime.now());

        // 예약 생성
        Reservation reservation = reservationDto.toEntity(member, restaurant);
        reservationRepository.save(reservation);

        // 리뷰 생성
        String comment = "comment";
        Double rating = 1.0;
        ReviewDto reviewDto = createReviewDto(comment, rating);

        // when
        Review review = reviewService.createReview(reservation, reviewDto);

        // then
        assertThat(review.getComment()).isEqualTo(comment);
        assertThat(review.getRating()).isEqualTo(rating);
    }

    @Test
    void 리뷰_조회() {
        // given
        // 멤버 생성
        MemberDto.SignUp memberAuth = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );

        Member member = memberRepository.save(memberAuth.toEntity());

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDate(LocalDate.now());
        reservationDto.setTime(LocalTime.now());

        // 예약 생성
        Reservation reservation = reservationDto.toEntity(member, restaurant);
        Reservation savedReservation = reservationRepository.save(reservation);

        // 리뷰 생성
        String comment = "comment";
        Double rating = 1.0;
        ReviewDto reviewDto = createReviewDto(comment, rating);

        Review review = reviewService.createReview(savedReservation, reviewDto);

        // when
        Review findReview = reviewService.getReview(review.getId());

        // then
        assertThat(findReview.getComment()).isEqualTo(comment);
        assertThat(findReview.getRating()).isEqualTo(rating);
    }

    @Test
    void 전체_리뷰_조회() {
        // given
        // 멤버 생성

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            members.add(
                    createMember("user" + (i + 1), "password", "jay" + (i + 1),
                            "0101111111" + (i + 1), Authority.USER).toEntity()
            );
        }

        memberRepository.saveAll(members);

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();

        // 예약 생성
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reservationDto.setDate(LocalDate.now().minusMonths(i));
            reservationDto.setTime(LocalTime.now());
            reservations.add(
                    reservationDto.toEntity(members.get(i), restaurant)
            );
        }
        reservationRepository.saveAll(reservations);

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRating(1.0 + i);
            reviewDto.setComment("comment" + (i + 1));
            reviewDtos.add(reviewDto);
        }

        // 리뷰 생성
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reviews.add(
                    reviewService.createReview(
                            reservations.get(i), reviewDtos.get(i)
                    )
            );
        }

        // when
        List<Review> findReviews = reviewService.getReviews(restaurant);

        // then
        assertThat(findReviews.size()).isEqualTo(4);
        assertThat(findReviews.get(0).getComment()).isEqualTo("comment1");
        assertThat(findReviews.get(1).getComment()).isEqualTo("comment2");
    }

    @Test
    void 리뷰_수정() {
        // given
        // 멤버 생성
        MemberDto.SignUp memberAuth = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );

        Member member = memberRepository.save(memberAuth.toEntity());

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDate(LocalDate.now());
        reservationDto.setTime(LocalTime.now());

        // 예약 생성
        Reservation reservation = reservationDto.toEntity(member, restaurant);
        reservationRepository.save(reservation);

        // 리뷰 생성
        String comment = "comment";
        Double rating = 1.0;
        ReviewDto reviewDto = createReviewDto(comment, rating);

        Review review = reviewService.createReview(reservation, reviewDto);

        // when
        Double newRating = 3.0;
        String newComment = "newComment";
        reviewService.updateReview(reservation, newRating, newComment);


        // then
        assertThat(review.getRating()).isEqualTo(newRating);
        assertThat(review.getComment()).isEqualTo(newComment);
    }

    @Test
    void 리뷰_평점_수정_반영() {
        // given
        // 멤버 생성
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            members.add(
                    createMember("user" + (i + 1), "password", "jay" + (i + 1),
                            "0101111111" + (i + 1), Authority.USER).toEntity()
            );
        }
        memberRepository.saveAll(members);

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();

        // 예약 생성
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reservationDto.setDate(LocalDate.now().minusMonths(i));
            reservationDto.setTime(LocalTime.now());
            reservations.add(
                    reservationDto.toEntity(members.get(i), restaurant)
            );
        }
        reservationRepository.saveAll(reservations);

        for (Reservation reservation : reservations) {
            System.out.println("reservation = " + reservation.getReview());
        }

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRating(1.0 + i);
            reviewDto.setComment("comment" + (i + 1));
            reviewDtos.add(reviewDto);
        }

        // 리뷰 생성
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reviews.add(
                    reviewService.createReview(
                            reservations.get(i), reviewDtos.get(i)
                    )
            );
        }

        // when
        Double newRating = 1.0;
        String newComment = "newComment";

        System.out.println("reservations.get(1).getReview() = " + reservations.get(1).getReview());
        reviewService.updateReview(reservations.get(2), newRating, newComment);

        // then
        assertThat(restaurant.getTotalRating()).isEqualTo(2.0);
    }

    @Test
    void 리뷰_평점_삭제_반영() {
        // given
        // 멤버 생성
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            members.add(
                    createMember("user" + (i + 1), "password", "jay" + (i + 1),
                            "0101111111" + (i + 1), Authority.USER).toEntity()
            );
        }
        memberRepository.saveAll(members);

        // 매장 생성
        RestaurantDto restaurantDto = createRestaurantDto(
                "매장1", "매장설명");
        Restaurant restaurant = restaurantRepository.save(restaurantDto.toEntity());

        ReservationDto reservationDto = new ReservationDto();

        // 예약 생성
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reservationDto.setDate(LocalDate.now().minusMonths(i));
            reservationDto.setTime(LocalTime.now());
            reservations.add(
                    reservationDto.toEntity(members.get(i), restaurant)
            );
        }
        reservationRepository.saveAll(reservations);

        for (Reservation reservation : reservations) {
            System.out.println("reservation = " + reservation.getReview());
        }

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRating(1.0 + i);
            reviewDto.setComment("comment" + (i + 1));
            reviewDtos.add(reviewDto);
        }

        // 리뷰 생성
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            reviews.add(
                    reviewService.createReview(
                            reservations.get(i), reviewDtos.get(i)
                    )
            );
        }

        // when
        reviewService.deleteReview(reservations.get(3).getId());

        // then
        assertThat(restaurant.getTotalRating()).isEqualTo(2.0);
        assertThat(restaurant.getReviews().size()).isEqualTo(3);
    }

    private static ReviewDto createReviewDto(String comment, Double rating) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setComment(comment);
        reviewDto.setRating(rating);
        return reviewDto;
    }

    private static MemberDto.SignUp createMember(String loginId, String password, String username, String phoneNumber, Authority authority) {
        MemberDto.SignUp memberAuth = new MemberDto.SignUp();
        memberAuth.setLoginId(loginId);
        memberAuth.setPassword(password);
        memberAuth.setUsername(username);
        memberAuth.setPhoneNumber(phoneNumber);
        memberAuth.setAuthority(authority);
        return memberAuth;
    }

    private static RestaurantDto createRestaurantDto(String name, String description) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(name);
        restaurantDto.setDescription(description);
        return restaurantDto;
    }
}
