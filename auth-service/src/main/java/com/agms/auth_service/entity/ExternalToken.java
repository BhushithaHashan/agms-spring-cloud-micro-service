package com.agms.auth_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ExternalToken {
    @Id
    private Long id = 1L; // We only need one row to cache the current token
    private String accessToken;
    private LocalDateTime expiryTime;
}