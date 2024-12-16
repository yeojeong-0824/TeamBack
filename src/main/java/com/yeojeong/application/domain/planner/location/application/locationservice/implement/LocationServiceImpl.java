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
    @Transactional
    public Location save(Location entity) {
        return locationRepository.save(entity);
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->  new NotFoundDataException("해당 location 을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Location update(Location entity) {
        return locationRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Location entity) {
        locationRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteByPlannerId(Long plannerId) {
        locationRepository.deleteByPlannerId(plannerId);
    }

    @Override
    public List<Location> findByMemberAndDate(Long memberId, Long start, Long end) {
        return locationRepository.findByMemberAndDate(memberId, start, end);
    }

    @Override
    public List<Location> findByPlannerId(Long plannerId, Long memberId) {
        return locationRepository.findByMemberAndPlanner(memberId, plannerId);
    }

    @Override
    @Transactional
    public void deleteByMemberId(Long memberId) {
        locationRepository.deleteByMemberId(memberId);
    }
}
