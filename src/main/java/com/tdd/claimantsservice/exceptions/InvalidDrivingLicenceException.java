package com.tdd.claimantsservice.exceptions;

public class InvalidDrivingLicenceException extends RuntimeException {
    public InvalidDrivingLicenceException(String message) {
        super(message);
    }
}
