package com.agms.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password; // Will be stored as BCrypt hash
    private String email;
}