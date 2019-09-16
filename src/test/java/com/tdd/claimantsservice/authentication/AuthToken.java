package com.tdd.claimantsservice.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a Bearer token received.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class AuthToken {
    private String token;
}
