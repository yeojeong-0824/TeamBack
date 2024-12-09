package com.yeojeong.application.domain.planner.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByPlannerIdOrderByUnixTime(Long plannerId);

    void deleteByPlannerId(Long plannerId);
}
