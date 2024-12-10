package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class NotFoundRestaurantException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "해당 매장을 찾을 수 없습니다.";
    }
}
