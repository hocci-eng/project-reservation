package zerobase.projectreservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralException(Exception e) {
        log.error("Global Exception 예외 발생: {}", e.getMessage());
        return new ErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
