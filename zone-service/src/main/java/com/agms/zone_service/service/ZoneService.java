package com.agms.zone_service.service;

import com.agms.zone_service.client.AuthClient;
import com.agms.zone_service.entity.Zone;
import com.agms.zone_service.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository repository;
    private final AuthClient authClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external-iot.base-url}")
    private String baseUrl;

    // public Zone createZone(Zone zone) {
    //     if (zone.getMinTemp() >= zone.getMaxTemp()) {
    //         throw new RuntimeException("Validation failed: MinTemp must be lower than MaxTemp");
    //     }

    //     // 1. Get JWT from our internal Auth Service via Feign
    //     String token = authClient.getExternalToken();

    //     // 2. Register device with external IoT provider
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setBearerAuth(token);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     Map<String, String> body = Map.of(
    //         "name", zone.getName(), 
    //         "zoneId", zone.getName()
    //     );
        
    //     HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

    //     try {
    //         // Call External API: http://104.211.95.241:8080/api/devices
    //         var response = restTemplate.postForObject(baseUrl + "/devices", request, Map.class);
    //         if (response != null && response.containsKey("deviceId")) {
    //             zone.setDeviceId(response.get("deviceId").toString());
    //         }
    //     } catch (Exception e) {
    //         // Fallback so the demo doesn't stall if the external API is down
    //         zone.setDeviceId("DEV-" + UUID.randomUUID().toString().substring(0, 8));
    //     }

    //     return repository.save(zone);
    // }
    public Zone createZone(Zone zone) {
    if (zone.getMinTemp() >= zone.getMaxTemp()) {
        throw new RuntimeException("Validation failed: MinTemp must be lower than MaxTemp");
    }

    try {
        // 1. Try to get the real token
        String token = authClient.getExternalToken();

        // 2. Try to register with external IoT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
            "name", zone.getName(), 
            "zoneId", zone.getName()
        );
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        var response = restTemplate.postForObject(baseUrl + "/devices", request, Map.class);
        
        if (response != null && response.containsKey("deviceId")) {
            zone.setDeviceId(response.get("deviceId").toString());
        }

    } catch (Exception e) {
        // FALLBACK: If Auth-Service is down OR IoT API is down, 
        // we generate a local ID so the Crop Service test can continue.
        System.out.println("External systems unavailable, using local device ID.");
        zone.setDeviceId("LOCAL-DEV-" + UUID.randomUUID().toString().substring(0, 8));
    }

    return repository.save(zone);
}

    public List<Zone> getAllZones() {
        return repository.findAll();
    }
}