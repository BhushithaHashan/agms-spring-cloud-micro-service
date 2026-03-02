package com.agms.zone_service.controller;

import com.agms.zone_service.entity.Zone;
import com.agms.zone_service.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor // This handles the injection for 'service' automatically
public class ZoneController {

    private final ZoneService service;

    @PostMapping
    public Zone save(@RequestBody Zone zone) {
        return service.createZone(zone);
    }

    @GetMapping
    public List<Zone> getAll() {
        // FIXED: Calling service method instead of accessing private repository
        return service.getAllZones();
    }
    @GetMapping("/{id}")
    public Zone getById(@PathVariable Long id) {
        return service.getAllZones().stream()
                .filter(z -> z.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));
    }
}