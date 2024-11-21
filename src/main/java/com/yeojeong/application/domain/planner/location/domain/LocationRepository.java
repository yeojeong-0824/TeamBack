package com.yeojeong.application.domain.planner.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByPlannerId(Long plannerId);
}
