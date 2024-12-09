package com.yeojeong.application.domain.planner.location.application.locationservice;

import com.yeojeong.application.domain.planner.location.domain.Location;

import java.util.List;

public interface LocationService {
    Location save(Location entity);
    Location findById(Long id);
    Location update(Location entity);
    void delete(Location entity);
    List<Location> findByPlannerId(Long plannerId);
    void deleteByPlannerId(Long plannerId);
}
