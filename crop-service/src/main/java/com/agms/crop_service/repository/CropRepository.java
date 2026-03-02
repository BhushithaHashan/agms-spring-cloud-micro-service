package com.agms.crop_service.repository;

import com.agms.crop_service.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    boolean existsByZoneId(Long zoneId);
    Optional<Crop> findByZoneId(Long zoneId);
}