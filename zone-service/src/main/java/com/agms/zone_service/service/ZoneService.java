package com.agms.zone_service.service;

import com.agms.zone_service.entity.Zone;
import com.agms.zone_service.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository repository;

    public List<Zone> getAllZones() {
        return repository.findAll();
    }

    public Zone createZone(Zone zone) {
        return repository.save(zone);
    }
}