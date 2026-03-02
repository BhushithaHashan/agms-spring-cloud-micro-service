package com.agms.crop_service.service;

import com.agms.crop_service.client.ZoneClient;
import com.agms.crop_service.entity.Crop;
import com.agms.crop_service.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CropService {
    private final CropRepository repository;
    private final ZoneClient zoneClient;

    public Crop saveCrop(Crop crop) {
        //Check if the Zone actually exists in the other service
        try {
            zoneClient.checkZoneExists(crop.getZoneId());
        } catch (Exception e) {
            throw new RuntimeException("Zone " + crop.getZoneId() + " not found in Zone Service!");
        }

        // Ensure the zone isn't already taken
        if (repository.existsByZoneId(crop.getZoneId())) {
            throw new RuntimeException("This zone already has a crop assigned!");
        }

        return repository.save(crop);
    }

    public List<Crop> getAll() { return repository.findAll(); }
    
    public Crop getByZone(Long zoneId) {
        return repository.findByZoneId(zoneId)
                .orElseThrow(() -> new RuntimeException("No crop in this zone"));
    }
}