package com.yeojeong.application.domain.planner.location.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findAllByPlannerId(Long plannerId, Pageable pageable);
}
