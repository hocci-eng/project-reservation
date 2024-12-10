package zerobase.projectreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Review;
import zerobase.projectreservation.domain.type.ArriveStatus;
import zerobase.projectreservation.dto.ReviewDto;
import zerobase.projectreservation.exception.impl.UnauthorizedCreationReviewException;
import zerobase.projectreservation.service.ReviewService;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성
     * 권한 -> member
     * 매장에 도착하지 않고 리뷰 작성 불가
     */
    @PostMapping("/member")
    public ResponseEntity<?> createReview(@RequestBody ReviewDto reviewDto) {
        Member member = getMember();

        ArriveStatus arriveStatus = member.getReservation().getArriveStatus();
        if (arriveStatus == ArriveStatus.NOT_ARRIVED) {
            throw new UnauthorizedCreationReviewException();
        }

        Review review = reviewService.createReview(member, reviewDto);

        return ResponseEntity.ok(review.toString());
    }

    /**
     * 리뷰 조회
     * 권한 -> 모든 사람
     */
    @GetMapping("{restaurantName}")
    public ResponseEntity<?> getReviews(@PathVariable String restaurantName,
                                        Pageable pageable) {
        Page<ReviewDto> reviews = reviewService.getReviews(restaurantName, pageable);

        return ResponseEntity.ok(reviews);
    }

    /**
     * 리뷰 수정
     * 권한 -> member
     */
    @PostMapping("/member/edit")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDto reviewDto) {
        Member member = getMember();
        ReviewDto result = reviewService.updateReview(member, reviewDto);

        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 삭제
     * 권한 -> 리뷰를 작성한 사람
     */
    @DeleteMapping("/member")
    public ResponseEntity<?> deleteReviewByMember() {
        Member member = getMember();
        reviewService.deleteReview(member);

        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 삭제
     * 권한 -> admin
     */
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<?> deleteReviewByAdmin(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }

    /**
     * 인증된 사용자의 정보 가져오기
     */
    private static Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (Member) authentication.getPrincipal();
    }
}
