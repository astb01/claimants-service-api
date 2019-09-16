package com.tdd.claimantsservice.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class InvalidRefNoException extends RuntimeException {

    public InvalidRefNoException(String message) {
        super(message);
    }

}
