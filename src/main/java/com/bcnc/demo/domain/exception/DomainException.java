package com.bcnc.demo.domain.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {

    private final HttpStatus status;

    protected DomainException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    protected DomainException(HttpStatus status) {
        super();
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
