package com.agms.zone_service.controller;

import com.agms.zone_service.entity.Zone;
import com.agms.zone_service.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<Zone>> getAll() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody Zone zone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createZone(zone));
    }
}