package com.yeojeong.application.domain.planner.location.application;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.OwnershipException;
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
                .orElseThrow(() ->  new NotFoundDataException("해당 location을 찾을 수 없습니다."));
    }

    public Location findByIdAuth(Long id, Long memberId) {
        Location entity = findById(id);
        if(!entity.getMember().getId().equals(memberId)) throw new OwnershipException("location을 작성한 사용자가 아닙니다.");
        return entity;
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
}
