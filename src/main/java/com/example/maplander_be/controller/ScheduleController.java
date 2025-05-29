package com.example.maplander_be.controller;

import com.example.maplander_be.dto.CreateGroupRequestDto;
import com.example.maplander_be.dto.CreateScheduleRequestDto;
import com.example.maplander_be.dto.ScheduleResponseDto;
import com.example.maplander_be.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    public final ScheduleService svc;

    public ScheduleController(ScheduleService svc){
        this.svc = svc;
    }

    // 스케줄 생성
    @PostMapping("/groups/{groupId}")
    public ResponseEntity<ScheduleResponseDto> create(
            @PathVariable Integer groupId,
            @RequestBody CreateScheduleRequestDto req, HttpSession session) {
        Integer me = (Integer) session.getAttribute("LOGIN_USER");
        ScheduleResponseDto dto = svc.create(me, groupId, req);
        return ResponseEntity.status(201).body(dto);
    }

    // 그룹별 스케줄 조회
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<ScheduleResponseDto>> listByGroup(
            @PathVariable Integer groupId, HttpSession session) {
        session.getAttribute("LOGIN_USER");
        List<ScheduleResponseDto> list = svc.listByGroup(groupId);
        return ResponseEntity.ok(list);
    }

    // 스케쥴 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer scheduleId, HttpSession session){
        session.getAttribute("LOGIN_USER");
        svc.delete(scheduleId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> update(

            @PathVariable Integer scheduleId,
            @RequestBody CreateGroupRequestDto req, HttpSession session)

    {
        Integer me = (Integer) session.getAttribute("LOGIN_USER");

        ScheduleResponseDto dto = svc.update(scheduleId, me, req);
        return ResponseEntity.ok(dto);

    }


}
