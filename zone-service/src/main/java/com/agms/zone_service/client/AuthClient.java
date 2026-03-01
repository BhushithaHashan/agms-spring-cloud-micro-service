package com.agms.zone_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// "AUTH-SERVICE" must match the name registered in Eureka
@FeignClient(name = "AUTH-SERVICE") 
public interface AuthClient {

    // This must match the endpoint you created in the Auth Service Controller
    @GetMapping("/api/auth/token")
    String getExternalToken();
}