package com.example.springsecurity.controller;

import com.example.springsecurity.dto.SignUpRequestDto;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign")
    public ResponseEntity<Long> signup(
            @RequestBody SignUpRequestDto signupRequestDto){
        try {
            userService.sign(signupRequestDto);
            String redirect = "/";
            return ResponseEntity.ok()
                    .header("Location", redirect)
                    .build();

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/admin")
    public ResponseEntity<String> admin(){

        return ResponseEntity.ok().body("ok");
    }
}
