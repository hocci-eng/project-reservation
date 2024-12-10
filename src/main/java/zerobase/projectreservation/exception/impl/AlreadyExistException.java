package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class AlreadyExistException extends AbstractException {
    @Override
    public String getMessage() {
        return "이미 존재하는 회원입니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }
}
