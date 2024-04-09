package me.eskim.springbootdeveloper.config.error.exception;

import me.eskim.springbootdeveloper.config.error.ErrorCode;

public class NotFoundException extends BusinessBaseException{

    public NotFoundException(ErrorCode errorCode) {

        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException() {

        super(ErrorCode.NOT_FOUND);
    }
}
