package com.agms.auth_service.service;

import com.agms.auth_service.entity.ExternalToken;
import com.agms.auth_service.entity.UserCredential;
import com.agms.auth_service.repository.TokenRepository;
import com.agms.auth_service.repository.UserCredentialRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${external-iot.username}") private String extUsername;
    @Value("${external-iot.password}") private String extPassword;
    @Value("${external-iot.base-url}") private String baseUrl;

    private final TokenRepository tokenRepository;
    private final UserCredentialRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

    // --- INTERNAL APP AUTH (JWT) ---

    public String registerInternalUser(UserCredential user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }

    public String generateInternalToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 Hour
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validatePassword(String username, String rawPassword) {
        UserCredential user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }

    // --- EXTERNAL IOT AUTH (Your Original Code) ---

    public String getExternalIotToken() {
        var cached = tokenRepository.findById(1L);
        if (cached.isPresent() && cached.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            return cached.get().getAccessToken();
        }
        try {
            return performExternalLogin();
        } catch (Exception e) {
            performExternalRegister();
            return performExternalLogin();
        }
    }

    private String performExternalLogin() {
        Map<String, String> body = Map.of("username", extUsername, "password", extPassword);
        var response = restTemplate.postForObject(baseUrl + "/auth/login", body, Map.class);
        String token = (String) response.get("accessToken");
        
        ExternalToken t = new ExternalToken();
        t.setId(1L);
        t.setAccessToken(token);
        t.setExpiryTime(LocalDateTime.now().plusHours(1));
        tokenRepository.save(t);
        return token;
    }

    private void performExternalRegister() {
        Map<String, String> body = Map.of("username", extUsername, "password", extPassword);
        restTemplate.postForObject(baseUrl + "/auth/register", body, Map.class);
    }
}