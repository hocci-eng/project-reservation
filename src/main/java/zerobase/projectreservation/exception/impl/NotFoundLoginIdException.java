package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class NotFoundLoginIdException extends AbstractException {
    @Override
    public String getMessage() {
        return "존재하지 않는 ID 입니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
