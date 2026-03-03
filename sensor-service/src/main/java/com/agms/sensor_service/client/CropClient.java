package com.agms.sensor_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "CROP-SERVICE")
public interface CropClient {
    @GetMapping("/api/crops")
    List<Map<String, Object>> getAllCrops();
}