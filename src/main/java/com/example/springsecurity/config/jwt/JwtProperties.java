package com.example.springsecurity.config.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;

    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_STRING = "Authorization";


//    @PostConstruct
//    public void init() {
//        System.out.println("prefix: " + TOKEN_PREFIX); // This should print the resolved JWT secret
//    }
}
