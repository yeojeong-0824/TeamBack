package com.yeojeong.application.domain.planner.location.application.locationfacade.implement;

import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.planner.location.application.locationfacade.LocationFacade;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationFacadeImpl implements LocationFacade {

    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    @Transactional
    public LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId) {
        Planner planner = plannerService.findById(plannerId);

        Location entity = LocationRequest.Save.toEntity(dto, planner);

        Location savedEntity = locationService.save(entity);

        if (planner.getLocationCount() >= 15) {
            throw new RequestDataException("Location 은 15개까지 생성 가능합니다.");
        }

        planner.addLocation();
        plannerService.save(planner);

        return LocationResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Location savedEntity = locationService.findById(id);
        Planner planner = plannerService.findById(savedEntity.getPlanner().getId());

        locationService.delete(savedEntity);

        planner.deleteLocation();
        plannerService.save(planner);

    }

    @Override
    @Transactional
    public LocationResponse.FindById update(LocationRequest.Put dto, Long id) {
        Location savedEntity = locationService.findById(id);

        savedEntity.update(dto);

        Location rtnEntity = locationService.update(savedEntity);
        return LocationResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public LocationResponse.FindById findById(Long id) {
        locationService.findById(id);
        return LocationResponse.FindById.toDto(locationService.findById(id));
    }

    @Override
    public List<LocationResponse.FindById> findByPlannerId(Long plannerId) {
        List<Location> locationList = locationService.findByPlannerId(plannerId);
        return locationList.stream()
                .map(LocationResponse.FindById::toDto)
                .collect(Collectors.toList());
    }
}
