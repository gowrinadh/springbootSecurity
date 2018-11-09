package com.test.security;

public class SecurityConstants {
	public static final String SECRET = "Auth_Demo";
    public static final long EXPIRATION_TIME = 30000; 	// 3 minutes
    public static final String TOKEN_PREFIX = "Basic ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTH_URL = "/user/auth/**";
}
