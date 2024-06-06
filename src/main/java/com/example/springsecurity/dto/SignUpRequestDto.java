package com.example.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {
    private String name;
    private String email;
    private String checkPassword;
    private String password;
    private String role;
    @Builder
    public static SignUpRequestDto of(String name, String email, String password, String checkPassword, String role){
        return SignUpRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .checkPassword(checkPassword)
                .role(role)
                .build();
    }
}
