package ru.otus.hw.exception;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    private final HttpStatus status;

    public RestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RestException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
