package com.example.maplander_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateScheduleRequestDto(

        @NotBlank String title,
        @NotNull LocalDateTime startDatetime,
        @NotNull LocalDateTime endDatetime,
        String description,
        Double latitude,
        Double longitude

) {}
