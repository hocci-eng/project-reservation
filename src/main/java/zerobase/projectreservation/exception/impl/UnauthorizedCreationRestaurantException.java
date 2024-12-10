package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class UnauthorizedCreationRestaurantException extends AbstractException {
    @Override
    public String getMessage() {
        return "리뷰 생성 권한이 없습니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
