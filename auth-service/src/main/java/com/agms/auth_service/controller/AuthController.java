package com.agms.auth_service.controller;

import com.agms.auth_service.entity.UserCredential;
import com.agms.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserCredential user) {
        return authService.registerInternalUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        authService.validatePassword(username, password);
        return authService.generateInternalToken(username);
    }

    @GetMapping("/external-iot-token")
    public String getIotToken() {
        return authService.getExternalIotToken();
    }
}