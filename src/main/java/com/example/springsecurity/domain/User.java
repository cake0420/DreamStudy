package com.example.springsecurity.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, name = "name")
    private String name;

    @Email
    @Column(unique = true, nullable = false, length = 200, name = "email")
    private String email;

    @Column(nullable = false, length = 200, name = "password")
    private String password;

    @Column(nullable = false, length = 50, name = "role")
    private String role;

    @Builder
    public User(String name, String email, String password, String role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
