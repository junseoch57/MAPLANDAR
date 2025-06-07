package com.example.maplander_be.client;

import com.example.maplander_be.dto.PlaceDto;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class KakaoMapClient {

    private final WebClient webClient;

    public KakaoMapClient(@Value("${kakao.rest-api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey)
                .build();
    }


    public Mono<List<PlaceDto>> searchPlaces(double lat, double lng,
                                             int radius, int size) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/category.json")
                        .queryParam("y", lat)
                        .queryParam("x", lng)
                        .queryParam("radius", radius)
                        .queryParam("category_group_code", "CE7") // 카페
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(PlaceDto.KakaoPlaceResponse.class)
                .map(PlaceDto.KakaoPlaceResponse::toPlaceDtoList);
    }


}
