package com.yeojeong.application.domain.planner.location.application.locationservice;

import com.yeojeong.application.domain.planner.location.domain.Location;

import java.util.List;

public interface LocationService {
    Location save(Location entity);
    Location findById(Long id);
    Location update(Location entity);
    void delete(Location entity);
    boolean existBefore(Integer year, Integer month, Integer day, Integer hour, Integer minute);
    boolean existAfter(Integer year, Integer month, Integer day, Integer hour, Integer minute);
    List<Location> findByPlannerId(Long plannerId);
}
