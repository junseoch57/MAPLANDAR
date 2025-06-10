package com.example.maplander_be.service;

import com.example.maplander_be.client.KakaoMapClient;
import com.example.maplander_be.dto.CoordinateDto;
import com.example.maplander_be.dto.PlaceDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LocationRecommendationService {

    private final KakaoMapClient kakaoMapClient;

    public LocationRecommendationService(KakaoMapClient kakaoMapClient) {
        this.kakaoMapClient = kakaoMapClient;
    }

    public Mono<List<PlaceDto>> recommend(List<CoordinateDto> coords){

        if (coords == null || coords.size() < 2){
            return Mono.error(new IllegalArgumentException("2개 이상의 좌표값을 입력해야 합니다."));
        }

        double avgLat = coords.stream().mapToDouble(CoordinateDto::latitude).average().orElseThrow();
        double avgLng = coords.stream().mapToDouble(CoordinateDto::longitude).average().orElseThrow();

        // 반경 2km, 20개 리턴 - 수정 필요함
        return kakaoMapClient.searchPlaces(avgLat, avgLng, 2000, 20);

    }


}
