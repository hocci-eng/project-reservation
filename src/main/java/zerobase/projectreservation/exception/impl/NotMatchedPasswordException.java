package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class NotMatchedPasswordException extends AbstractException {
    @Override
    public String getMessage() {
        return "비밀번호가 일치하지 않습니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
