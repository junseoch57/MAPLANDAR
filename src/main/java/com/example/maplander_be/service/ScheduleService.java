package com.example.maplander_be.service;

import com.example.maplander_be.domain.ListOfGroup;
import com.example.maplander_be.domain.Schedule;
import com.example.maplander_be.dto.CreateGroupRequestDto;
import com.example.maplander_be.dto.CreateScheduleRequestDto;
import com.example.maplander_be.dto.ScheduleResponseDto;
import com.example.maplander_be.repository.GroupRepository;
import com.example.maplander_be.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepo;
    private final GroupRepository groupRepo;

    public ScheduleService(ScheduleRepository scheduleRepo, GroupRepository groupRepo) {
        this.scheduleRepo = scheduleRepo;
        this.groupRepo = groupRepo;
    }


    // 스케줄 생성
    public ScheduleResponseDto create(Integer ownerId, Integer groupId, CreateScheduleRequestDto req) {
        ListOfGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"그룹이 없습니다."));
        Schedule sch = new Schedule(
                group, ownerId,
                req.title(),
                req.startDatetime(),
                req.endDatetime(),
                req.description(),
                req.latitude(),
                req.longitude()
        );
        Schedule saved = scheduleRepo.save(sch);
        return new ScheduleResponseDto(
                saved.getScheduleId(),
                saved.getGroup().getGroupId(),
                saved.getCreatorId(),
                saved.getTitle(),
                saved.getStartDatetime(),
                saved.getEndDatetime(),
                saved.getDescription(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    // 그룹별 스케줄 조회
    public List<ScheduleResponseDto> listByGroup(Integer groupId) {
        return scheduleRepo.findByGroupGroupId(groupId).stream()
                .map(s -> new ScheduleResponseDto(
                        s.getScheduleId(),
                        s.getGroup().getGroupId(),
                        s.getCreatorId(),
                        s.getTitle(),
                        s.getStartDatetime(),
                        s.getEndDatetime(),
                        s.getDescription(),
                        s.getLatitude(),
                        s.getLongitude(),
                        s.getCreatedAt(),
                        s.getUpdatedAt()
                ))
                .toList();
    }

    // 스케줄 삭제
    public void delete(Integer scheduleId) {

        scheduleRepo.deleteById(scheduleId);
    }


    public ScheduleResponseDto update(Integer scheduleId, Integer userId, CreateGroupRequestDto req){

        Schedule sch = scheduleRepo.findById(scheduleId).orElseThrow(()
                -> new IllegalArgumentException("일정을 찾을 수 없음"));

        if (!sch.getCreatorId().equals(userId)){
            throw new IllegalArgumentException("일정 수정 권한이 없음");
        }

        Schedule updated = scheduleRepo.save(sch);
        // Dto로 변환하고 리턴
        return new ScheduleResponseDto(
                updated.getScheduleId(),
                updated.getGroup().getGroupId(),
                updated.getCreatorId(),
                updated.getTitle(),
                updated.getStartDatetime(),
                updated.getEndDatetime(),
                updated.getDescription(),
                updated.getLatitude(),
                updated.getLongitude(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
                // DB처럼 함
        );

    }

}
