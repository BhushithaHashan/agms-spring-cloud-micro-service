package com.agms.crop_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double optimalTemp;
    
    @Column(unique = true) // Database level protection: One zone, one crop
    private Long zoneId;
}