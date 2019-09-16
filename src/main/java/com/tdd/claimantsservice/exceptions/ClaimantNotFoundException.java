package com.tdd.claimantsservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClaimantNotFoundException extends RuntimeException {

    public ClaimantNotFoundException(String message) {
        super(message);
    }
}
