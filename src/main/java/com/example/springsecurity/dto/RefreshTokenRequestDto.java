package com.example.springsecurity.dto;

import com.example.springsecurity.domain.RefreshToken;
import com.example.springsecurity.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDto {

    private User user;
    private String refresh;

    public static RefreshTokenRequestDto of(User user, String refresh){
        return RefreshTokenRequestDto.builder()
                .user(user)
                .refresh(refresh)
                .build();
    }

    public RefreshToken toEntity(User user, String refresh){
        return new RefreshToken(user, refresh);
    }
}
