package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.aop.OperationLoggable;
import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.dto.AuthDtos;
import com.example.villagefirerisk.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @OperationLoggable(module = "Auth", operation = "Register")
    public ApiResponse<String> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("注册成功");
    }

    @PostMapping("/login")
    @OperationLoggable(module = "Auth", operation = "Login")
    public ApiResponse<AuthDtos.LoginResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
