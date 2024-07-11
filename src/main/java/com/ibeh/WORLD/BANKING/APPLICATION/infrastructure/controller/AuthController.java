package com.ibeh.WORLD.BANKING.APPLICATION.infrastructure.controller;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.LoginRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.UserRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.ApiResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.JwtAuthResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public BankResponse createUserAccount(@Valid @RequestBody UserRequest userRequest) {
        return authService.registerUser(userRequest);
    }


    @PostMapping("/login-user")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        return authService.loginUser(loginRequest);

    }


}
