package com.ibeh.WORLD.BANKING.APPLICATION.service;


import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.LoginRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.UserRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.ApiResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {              //anything with authentitcation should be in the authservice

    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest);
}
