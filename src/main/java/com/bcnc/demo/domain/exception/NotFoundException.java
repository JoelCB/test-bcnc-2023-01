package com.bcnc.demo.domain.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
