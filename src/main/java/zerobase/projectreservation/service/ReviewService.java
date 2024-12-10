package zerobase.projectreservation.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.Review;
import zerobase.projectreservation.dto.ReviewDto;
import zerobase.projectreservation.exception.impl.NotFoundReservationException;
import zerobase.projectreservation.exception.impl.NotFoundRestaurantException;
import zerobase.projectreservation.exception.impl.NotFoundReviewException;
import zerobase.projectreservation.repository.ReservationRepository;
import zerobase.projectreservation.repository.RestaurantRepository;
import zerobase.projectreservation.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 생성
     * 권한 -> member
     * 예약 한 사람만 리뷰를 작성할 수 있음
     */
    public Review createReview(Member member, ReviewDto reviewDto) {
        Reservation reservation = getReservationByMember(member);

        Restaurant restaurant = reservation.getRestaurant();

        Review review = reviewDto.toEntity(restaurant);
        review.addReview(restaurant);
        review.setReservation(reservation);

        // 매장에 등록된 리뷰가 없으면 처음 리뷰의 점수를 매장의 총 리뷰점수로 설정
        if (restaurant.getReviews().size() == 1) {
            restaurant.setTotalRating(review.getRating());
        } else {
            Double newRating = review.calcAddRating();
            restaurant.setTotalRating(newRating);
        }

        return reviewRepository.save(review);
    }

    /**
     * 리뷰 조회
     */
    @Transactional(readOnly = true)
    public Review getReview(Long id) {
        return reviewRepository.findReviewById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("해당 ID의 리뷰가 없습니다.")
                );
    }

    /**
     * 해당 매장의 모든 리뷰 조회
     */
    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviews(String restaurantName, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findRestaurantByName(restaurantName)
                .orElseThrow(NotFoundRestaurantException::new);

        List<ReviewDto> reviews = restaurant.getReviews()
                .stream()
                .map(Review::toReviewDto)
                .toList();

        if (reviews.isEmpty()) {
            throw new IllegalStateException("등록된 리뷰가 없습니다.");
        }

        // 페이징 구현
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviews.size());
        List<ReviewDto> paginatedReviews = reviews.subList(start, end);

        return new PageImpl<>(paginatedReviews, pageable, reviews.size());
    }

    /**
     * 리뷰 수정
     * 예약 정보를 기반으로 리뷰를 작성한 사람만 수정 가능
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReviewDto updateReview(Member member, ReviewDto reviewDto) {
        Reservation reservation = getReservationByMember(member);

        // 예약 정보를 기반으로 리뷰 조회
        Review nowReview = reviewRepository.findReviewByReservationId(reservation.getId())
                .orElseThrow(() -> new IllegalStateException("작성한 리뷰가 없습니다."));

        // 레스토랑의 새로운 평균 평점 계산
        Double newRating = reviewDto.getRating();
        String newComment = reviewDto.getComment();

        Double updatedRating = nowReview.calcUpdateRating(newRating);
        nowReview.getRestaurant().setTotalRating(updatedRating);

        // 리뷰 내용 및 평점 업데이트
        nowReview.setRating(newRating);
        nowReview.setComment(newComment);

        // 저장
        return nowReview.toReviewDto();
    }


    /**
     * 리뷰 제거
     * member 사용
     */
    public void deleteReview(Member member) {
        Review review = reviewRepository.findReviewByMemberId(member.getId())
                .orElseThrow(NotFoundReviewException::new);

        // 삭제 시 현재 리뷰의 점수를 지운 상태로 업데이트
        Double updatedRating = review.calcRemoveRating();
        review.getRestaurant().setTotalRating(updatedRating);

        review.removeReview();
        reviewRepository.delete(review);
    }

    /**
     * 리뷰 제거
     * admin 사용
     * 리뷰 id를 기반으로 삭제
     */
    public void deleteReview(Long id) {
        Review review = reviewRepository.findReviewById(id)
                .orElseThrow(NotFoundReviewException::new);

        // 삭제 시 현재 리뷰의 점수를 지운 상태로 업데이트
        Double updatedRating = review.calcRemoveRating();
        review.getRestaurant().setTotalRating(updatedRating);

        review.removeReview();

        reviewRepository.delete(review);
    }

    /**
     * 사용자 정보로 예약 정보 찾기
     * 없으면 exception 발생
     */
    private Reservation getReservationByMember(Member member) {

        return reservationRepository.findReservationByMemberById(member.getId())
                .orElseThrow(NotFoundReservationException::new);
    }

}
