package com.agms.crop_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient   // Tells Eureka "I am a service, please list me"
@EnableFeignClients      // Tells Spring "Scan for @FeignClient interfaces and build them"
public class CropServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CropServiceApplication.class, args);
    }

}