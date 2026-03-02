package com.agms.crop_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ZONE-SERVICE")
public interface ZoneClient {
    // This calls the GET mapping  in ZoneController
    @GetMapping("/api/zones/{id}")
    Object checkZoneExists(@PathVariable("id") Long id);
}