package com.agms.auth_service.service;

import com.agms.auth_service.entity.ExternalToken;
import com.agms.auth_service.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {
    @Value("${external-iot.username}") private String username;
    @Value("${external-iot.password}") private String password;
    @Value("${external-iot.base-url}") private String baseUrl;

    private final TokenRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public AuthService(TokenRepository repository) { this.repository = repository; }

    public String getValidToken() {
        var cached = repository.findById(1L);
        if (cached.isPresent() && cached.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            return cached.get().getAccessToken();
        }

        try {
            return performLogin();
        } catch (Exception e) {
            performRegister();
            return performLogin();
        }
    }

    private String performLogin() {
        Map<String, String> body = Map.of("username", username, "password", password);
        var response = restTemplate.postForObject(baseUrl + "/auth/login", body, Map.class);
        String token = (String) response.get("accessToken");
        
        ExternalToken t = new ExternalToken();
        t.setId(1L);
        t.setAccessToken(token);
        t.setExpiryTime(LocalDateTime.now().plusHours(1));
        repository.save(t);
        return token;
    }

    private void performRegister() {
        Map<String, String> body = Map.of("username", username, "password", password);
        restTemplate.postForObject(baseUrl + "/auth/register", body, Map.class);
    }
}