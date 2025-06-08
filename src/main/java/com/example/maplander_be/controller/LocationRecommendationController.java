package com.example.maplander_be.controller;

import com.example.maplander_be.service.LocationRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
public class LocationRecommendationController {  // 아직 수정 중

    private final LocationRecommendationService svc;

    public LocationRecommendationController(LocationRecommendationService svc) {
        this.svc = svc;
    }




}
