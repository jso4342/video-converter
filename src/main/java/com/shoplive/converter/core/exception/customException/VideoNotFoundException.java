package com.shoplive.converter.core.exception.customException;

import com.shoplive.converter.core.exception.ErrorCode;

public class VideoNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public VideoNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
