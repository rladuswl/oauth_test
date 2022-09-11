package com.test.oauth_test.test;

public interface JwtProperties {
    String SECRET = "plovo";
    int EXPIRATION_TIME =  60000*10*3; // 30분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
