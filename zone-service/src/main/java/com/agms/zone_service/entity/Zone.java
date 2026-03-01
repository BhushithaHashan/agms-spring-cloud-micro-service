package com.agms.zone_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double minTemp; // Added for assignment logic
    private Double maxTemp; // Added for assignment logic
    private String deviceId; // This will hold the ID from the External IoT API
}