package com.example.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    private String email;
    private String password;

    @Builder
    public static LoginRequestDto of(String email, String password){
        return LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
