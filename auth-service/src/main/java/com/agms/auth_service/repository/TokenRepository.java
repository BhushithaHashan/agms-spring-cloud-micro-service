package com.agms.auth_service.repository;

import com.agms.auth_service.entity.ExternalToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<ExternalToken, Long> {
}