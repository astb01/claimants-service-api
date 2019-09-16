package com.tdd.claimantsservice.security;

public final class SecurityConstants {
    public static final int EXPIRY_TIME = 300000;
    public static final String SECRET = "someSecret";
    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGN_UP_URL = "/auth/register";
    public static final String LOGIN_URL = "/login";

}
