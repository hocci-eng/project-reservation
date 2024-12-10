package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class AlreadyExistReservationException extends AbstractException {
    @Override
    public String getMessage() {
        return "해당 시간에 예약이 불가능합니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }
}
