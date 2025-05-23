package com.example.maplander_be.repository;

import com.example.maplander_be.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    List<Schedule> findByGroupGroupId(Integer groupId);
    // Group : Schedule 엔티티의 group 필드
    // GroupId : ListOfGroup의 groupId


}
