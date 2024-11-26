package com.yeojeong.application.domain.planner.location.application.locationservice.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.domain.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location save(Location entity) {
        return locationRepository.save(entity);
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->  new NotFoundDataException("해당 location 을 찾을 수 없습니다."));
    }

    @Override
    public Location update(Location entity) {
        return locationRepository.save(entity);
    }

    @Override
    public void delete(Location entity) {
        locationRepository.delete(entity);
    }

    @Override
    public boolean existBefore(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        return locationRepository.existsBefore(year, month, day, hour, minute);
    }

    @Override
    public boolean existAfter(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        return locationRepository.existsAfter(year, month, day, hour, minute);
    }

    @Override
    public List<Location> findByPlanner(Long plannerId) {
        return locationRepository.findAllByPlannerId(plannerId);
    }
}
