package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class NotFoundReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "해당 예약을 찾을 수 없습니다.";
    }
}
