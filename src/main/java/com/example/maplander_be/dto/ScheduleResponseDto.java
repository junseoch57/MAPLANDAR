package com.example.maplander_be.dto;

import java.time.LocalDateTime;

public record ScheduleResponseDto(

        Integer scheduleId,
        Integer groupId,
        Integer creatorId,
        String title,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        String description,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
