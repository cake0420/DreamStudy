package com.example.springsecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecurity.domain.RefreshToken;
import com.example.springsecurity.domain.User;
import com.example.springsecurity.dto.RefreshTokenRequestDto;
import com.example.springsecurity.repository.RefreshTokenRepository;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String accessToken(String email, String role, String secret) {
        String token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000L)) //10분
                .withClaim("email", email)
                .withClaim("role", role) // Role 정보 추가
                .sign(Algorithm.HMAC512(secret));
        return token;
    }

    public String refreshToken(String email, String role, String secret) {
        String token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000L)) //24시간
                .withClaim("email", email)
                .withClaim("role", role) // Role 정보 추가
                .sign(Algorithm.HMAC512(secret));
        System.out.println("토큰 서비스 email: "+email);
        User user = userRepository.findByEmail(email);
        Optional<RefreshToken> refreshOptional = refreshTokenRepository.findByUser(user);
        if(refreshOptional.isPresent()){
            RefreshTokenRequestDto refresh = RefreshTokenRequestDto.builder()
                    .refresh(token)
                    .user(user)
                    .build();
            RefreshToken refreshToken = refreshOptional.get();
            refreshToken.updateRefreshToken(refresh);
            refreshTokenRepository.save(refreshToken);
        }else {
            refreshTokenRepository.save(new RefreshToken(user, token));
        }
        return token;
    }
}
