package zerobase.projectreservation.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.projectreservation.exception.AbstractException;

public class NotRegisteredException extends AbstractException {
    @Override
    public String getMessage() {
        return "등록된 회원 정보가 없습니다.";
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
