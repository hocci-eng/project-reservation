package zerobase.projectreservation.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.Review;
import zerobase.projectreservation.dto.ReviewDto;
import zerobase.projectreservation.repository.ReservationRepository;
import zerobase.projectreservation.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 리뷰 생성
     * 예약 한 사람만 리뷰를 작성할 수 있음
     */
    public Review createReview(Reservation reservation, ReviewDto reviewDto) {
        isExists(reservation);

        Restaurant restaurant = reservation.getRestaurant();

        Review review = reviewDto.toEntity(restaurant);
        review.addReview(restaurant);
        review.setReservation(reservation);


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
    public List<Review> getReviews(Restaurant restaurant) {
        if (restaurant == null) {
            throw new EntityNotFoundException("등록된 리뷰가 없습니다.");
        }
        return reviewRepository.findReviewsByRestaurantId(restaurant.getId());
    }

    /**
     * 리뷰 수정
     * 리뷰를 작성한 사람만
     */
    public void updateReview(Reservation reservation, Double newRating, String newComment) {
        // 리뷰 조회
        Review nowReview = reviewRepository.findReviewByReservationId(reservation.getId())
                .orElseThrow(() -> new IllegalStateException("작성한 리뷰가 없습니다."));

        // 레스토랑의 새로운 평균 평점 계산
        Double updatedRating = nowReview.calcUpdateRating(newRating);
        nowReview.getRestaurant().setTotalRating(updatedRating);

        // 리뷰 내용 및 평점 업데이트
        nowReview.setRating(newRating);
        nowReview.setComment(newComment);

        // 저장
        reviewRepository.save(nowReview);
    }

    /**
     * 리뷰 제거
     * 매장 점주, 유저 사용
     */
    public void deleteReview(Long id) {
        Review review = reviewRepository.findReviewById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 ID의 리뷰가 없습니다.")
        );

        Double updatedRating = review.calcRemoveRating();
        review.getRestaurant().setTotalRating(updatedRating);

        review.removeReview();

        reviewRepository.deleteReviewById(id);
    }

    /**
     * 사용자의 아이디로 예약을 했는지 검증
     */
    private void isExists(Reservation reservation) {
        boolean exists = reservationRepository.existsReservationById(
                reservation.getMember().getId());

        if (!exists) {
            throw new EntityNotFoundException("등록된 예약 정보가 없습니다.");
        }
    }

}
