package com.shoplive.converter.core.exception.customException;

import com.shoplive.converter.core.exception.ErrorCode;

public class ResizedNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public ResizedNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

