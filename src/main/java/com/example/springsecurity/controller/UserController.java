package com.example.springsecurity.controller;

import com.example.springsecurity.dto.SignUpRequestDto;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign")
    public ResponseEntity<Long> signup(
            @RequestBody SignUpRequestDto signupRequestDto){
        try {
            Long userId = userService.sign(signupRequestDto);
            String redirect = "/";
            return ResponseEntity.ok()
                    .header("Location", redirect)
                    .build();

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<Long> login(
//            @RequestBody LoginRequestDto loginRequestDto){
//        try {
//                if(userService.login(loginRequestDto) != null) {
//                    System.out.println("성공");
//                    return ResponseEntity.ok()
//                            .build();
//                }
//                else {
//                    System.out.println("실패");
//
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
//
//        } catch (IllegalArgumentException e) {
//            System.out.println("실패");
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/admin")
    public String admin(){
        return "amdin";
    }
}
