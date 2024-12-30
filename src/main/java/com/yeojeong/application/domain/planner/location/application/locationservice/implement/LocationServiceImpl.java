package com.yeojeong.application.domain.planner.location.application.locationservice.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.domain.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Location> findByMemberAndDate(Long memberId, Long start, Long end) {
        return locationRepository.findByMemberAndDate(memberId, start, end);
    }

    @Override
    public List<Location> findByPlannerId(Long plannerId) {
        return locationRepository.findByPlannerId(plannerId);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        locationRepository.deleteByMemberId(memberId);
    }

    @Override
    public void deleteByPlannerId(Long plannerId) {
        locationRepository.deleteByPlannerId(plannerId);
    }
}
