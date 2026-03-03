package com.agms.sensor_service.service;

import com.agms.sensor_service.client.CropClient;
import com.agms.sensor_service.client.ZoneClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

    private final CropClient cropClient;
    private final ZoneClient zoneClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external-iot.base-url}")
    private String iotBaseUrl;

    // Runs every 10,000 milliseconds (10 seconds)
    @Scheduled(fixedRate = 10000)
    public void checkSystemHealth() {
        log.info("=== MONITORING CYCLE START ===");

        try {
            // 1. Pull the Plan (What is growing?)
            List<Map<String, Object>> crops = cropClient.getAllCrops();
            if (crops.isEmpty()) {
                log.info("No crops found to monitor.");
                return;
            }

            for (Map<String, Object> crop : crops) {
                processCrop(crop);
            }

        } catch (Exception e) {
            log.error("Automation loop failed: {}", e.getMessage());
        }
    }

    private void processCrop(Map<String, Object> crop) {
        try {
            String cropName = crop.get("name").toString();
            Long zoneId = Long.valueOf(crop.get("zoneId").toString());
            Double targetTemp = Double.valueOf(crop.get("optimalTemp").toString());

            // 2. Get the Metadata (Where is it?)
            Map<String, Object> zone = zoneClient.getZoneById(zoneId);
            String deviceId = zone.get("deviceId").toString();

            // 3. Get Real-Time Data (What is the temp now?)
            String url = iotBaseUrl + "/devices/" + deviceId;
            Map<String, Object> status = restTemplate.getForObject(url, Map.class);

            if (status != null && status.containsKey("temperature")) {
                Double actualTemp = Double.valueOf(status.get("temperature").toString());
                compareAndAlert(cropName, zoneId, targetTemp, actualTemp);
            }

        } catch (Exception e) {
            log.warn("Could not process a crop entry: {}", e.getMessage());
        }
    }

    private void compareAndAlert(String name, Long zone, Double target, Double actual) {
        double diff = actual - target;

        if (Math.abs(diff) > 2.0) {
            log.warn(" ALERT | Zone {}: {} is STRESSED! Current: {}°C (Ideal: {}°C)", 
                     zone, name, actual, target);
        } else {
            log.info(" OK | Zone {}: {} is healthy at {}°C", zone, name, actual);
        }
    }
}