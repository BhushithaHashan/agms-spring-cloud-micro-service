package com.agms.crop_service.controller;

import com.agms.crop_service.entity.Crop;
import com.agms.crop_service.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {
    private final CropService service;

    @PostMapping
    public Crop create(@RequestBody Crop crop) {
        return service.saveCrop(crop);
    }

    @GetMapping
    public List<Crop> getAll() {
        return service.getAll();
    }

    @GetMapping("/zone/{zoneId}")
    public Crop getByZone(@PathVariable Long zoneId) {
        return service.getByZone(zoneId);
    }
}