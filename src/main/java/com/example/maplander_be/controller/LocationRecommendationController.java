package com.example.maplander_be.controller;

import com.example.maplander_be.dto.CoordinateDto;
import com.example.maplander_be.dto.PlaceDto;
import com.example.maplander_be.service.LocationRecommendationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationRecommendationController {  // 아직 수정 중

    private final LocationRecommendationService svc;

    public LocationRecommendationController(LocationRecommendationService svc) {
        this.svc = svc;
    }


    /* 장소 추천 요청 */
    @PostMapping("/recommend")
    public Mono<ResponseEntity<List<PlaceDto>>> recommend(
            @RequestBody @Valid @Size(min = 2) List<CoordinateDto> coordinates) {

        return svc.recommend(coordinates)
                .map(ResponseEntity::ok);

    }
}
