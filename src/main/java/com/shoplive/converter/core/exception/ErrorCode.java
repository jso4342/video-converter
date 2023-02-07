package com.shoplive.converter.core.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Parameter error"),

    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, "video does not exist"),

    ORIGINAL_NOT_FOUND(HttpStatus.NOT_FOUND, "original video does not exist"),

    RESIZED_NOT_FOUND(HttpStatus.NOT_FOUND, "resized video does not exist"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "server error"),

    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "file must not exceed 100MB in size"),

    UNSUPPORTED_FORMAT(HttpStatus.BAD_REQUEST, "file must be in MP4");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
