package com.example.maplander_be.dto;

import java.util.List;

public record RecommendationResponseDto(

        List<String> userNames,
        List<PlaceDto> recommendedPlaces,
        String title

){
}
