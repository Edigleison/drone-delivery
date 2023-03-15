package com.velozient.dronedeliveryapi.config.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleAll(final RuntimeException ex, final WebRequest request) {
        final String message = "Error on process input file. " + ex.getLocalizedMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, "error occurred");
        return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
