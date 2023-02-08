package com.shoplive.converter.core.exception;

import com.shoplive.converter.core.exception.customException.OriginalNotFoundException;
import com.shoplive.converter.core.exception.customException.ResizedNotFoundException;
import com.shoplive.converter.core.exception.customException.UnsupportedFormatException;
import com.shoplive.converter.core.exception.customException.VideoNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.Iterator;

import static com.shoplive.converter.core.exception.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e) {
        ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleException(
            MissingServletRequestParameterException e) {
        ErrorResponse response = ErrorResponse.of(INVALID_PARAMETER);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleException(
            MultipartException e) {
        ErrorResponse response = ErrorResponse.of(PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(UnsupportedFormatException.class)
    protected ResponseEntity<ErrorResponse> handleException(
            UnsupportedFormatException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getStatus(),
                e.getErrorCode().getMessage());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleException(
            VideoNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getStatus(),
                e.getErrorCode().getMessage());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @ExceptionHandler(OriginalNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleException(
            OriginalNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getStatus(),
                e.getErrorCode().getMessage());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @ExceptionHandler(ResizedNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleException(
            ResizedNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getStatus(),
                e.getErrorCode().getMessage());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleException(
            ConstraintViolationException e) {
        String resultMessage = getResultMessage(e.getConstraintViolations().iterator());
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, resultMessage);
        return ResponseEntity.badRequest().body(response);
    }

    protected String getResultMessage(final Iterator<ConstraintViolation<?>> violationIterator) {
        final StringBuilder resultMessageBuilder = new StringBuilder();
        while (violationIterator.hasNext()) {
            final ConstraintViolation<?> constraintViolation = violationIterator.next();
            resultMessageBuilder
                    .append("['")
                    .append(getPropertyName(constraintViolation.getPropertyPath().toString()))
                    .append("' is '")
                    .append(constraintViolation.getInvalidValue())
                    .append("'. ")
                    .append(constraintViolation.getMessage())
                    .append("]");

            if (violationIterator.hasNext()) {
                resultMessageBuilder.append(", ");
            }
        }

        return resultMessageBuilder.toString();
    }

    protected String getPropertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
    }
}
