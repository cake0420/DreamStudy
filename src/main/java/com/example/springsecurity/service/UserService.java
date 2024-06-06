package com.example.springsecurity.service;

import com.example.springsecurity.domain.User;
import com.example.springsecurity.dto.SignUpRequestDto;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder  bCryptPasswordEncoder;


    @Transactional
    public Long sign(SignUpRequestDto signupRequestDto){
        String name = signupRequestDto.getName();
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getCheckPassword();
        String checkPassword = signupRequestDto.getPassword();
        String role = signupRequestDto.getRole();
        if(Objects.equals(password, checkPassword) && !userRepository.existsByEmail(email)){

            User user = userRepository.save(new User(name, email, bCryptPasswordEncoder.encode(password), role));
            return user.getId();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}
