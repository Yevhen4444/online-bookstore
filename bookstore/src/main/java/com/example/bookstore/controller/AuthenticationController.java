package com.example.bookstore.controller;

import com.example.bookstore.dto.UserLoginRequestDto;
import com.example.bookstore.dto.UserLoginResponseDto;
import com.example.bookstore.dto.UserRegistrationRequestDto;
import com.example.bookstore.dto.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.service.UserService;
import com.example.bookstore.service.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication management",
        description = "Endpoints for user registration")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account in the system")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request)
                throws RegistrationException {
        return userService.register(request);
    }
    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates user and returns JWT token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }
}
