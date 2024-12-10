package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class InvalidTokenException extends AbstractException {
    @Override
    public String getMessage() {
        return "토큰값이 만료되었습니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
