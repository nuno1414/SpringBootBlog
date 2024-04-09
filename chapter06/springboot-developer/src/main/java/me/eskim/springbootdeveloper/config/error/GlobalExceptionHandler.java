package me.eskim.springbootdeveloper.config.error;

import lombok.extern.slf4j.Slf4j;
import me.eskim.springbootdeveloper.config.error.exception.BusinessBaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 중앙에서 한꺼번에 처리 할 수 있음
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
        log.error("BusinessBaseException" ,e);
        return createErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace();
        log.error("Exception", e);
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handle(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return createErrorResponseEntity(ErrorCode.ILLEGAL_ARGUMENT);
    }
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {

        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }
}

