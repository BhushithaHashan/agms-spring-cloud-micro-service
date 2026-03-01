package com.agms.auth_service.controller;

import com.agms.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token")
    public String getToken() {
        // This calls the service logic we wrote earlier (Login -> Register -> Login)
        return authService.getValidToken();
    }
}