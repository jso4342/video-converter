package com.shoplive.converter.core.exception.customException;

import com.shoplive.converter.core.exception.ErrorCode;

public class UnsupportedFormatException extends RuntimeException{

    private final ErrorCode errorCode;

    public UnsupportedFormatException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

