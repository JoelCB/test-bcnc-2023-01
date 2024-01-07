package com.bcnc.demo.application.rest;

import com.bcnc.demo.application.rest.response.ErrorResponse;
import com.bcnc.demo.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<ErrorResponse> handleDomainExceptionException(DomainException ex) {

        return buildErrorResponse(ex, ex.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleDefaultException(Exception ex) {

        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {

        ErrorResponse response = ErrorResponse.builder()
                .date(LocalDateTime.now())
                .status(status.value())
                .description(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, status);
    }
}
