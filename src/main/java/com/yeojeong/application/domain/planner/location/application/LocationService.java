package com.yeojeong.application.domain.planner.location.application;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.domain.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location save(Location entity) {
        return locationRepository.save(entity);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->  new NotFoundDataException("해당 location 을 찾을 수 없습니다."));
    }

    public Location update(Location entity, Location updateEntity) {
        entity.update(updateEntity);
        return locationRepository.save(entity);
    }

    public void delete(Location entity) {
        locationRepository.delete(entity);
    }

    public List<Location> findByMemberAndDate(Long memberId, Long start, Long end) {
        return locationRepository.findByMemberAndDate(memberId, start, end);
    }

    public List<Location> findByPlannerId(Long plannerId) {
        return locationRepository.findByPlannerId(plannerId);
    }

    public void deleteByMemberId(Long memberId) {
        locationRepository.deleteByMemberId(memberId);
    }

    public void deleteByPlannerId(Long plannerId) {
        locationRepository.deleteByPlannerId(plannerId);
    }
}
