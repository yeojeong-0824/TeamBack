package com.yeojeong.application.domain.planner.location.application.locationfacade.implement;

import com.yeojeong.application.domain.planner.location.application.locationfacade.LocationFacade;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationFacadeImpl implements LocationFacade {

    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    public LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId) {
        Planner planner = plannerService.findById(plannerId);

        Location entity = LocationRequest.Save.toEntity(dto, planner);
        Location savedEntity = locationService.save(entity);

        return LocationResponse.FindById.toDto(savedEntity);
    }

    @Override
    public void delete(Long id) {
        Location savedEntity = locationService.findById(id);
        locationService.delete(savedEntity);
    }

    @Override
    public LocationResponse.FindById update(LocationRequest.Put dto, Long id) {
        Location savedEntity = locationService.findById(id);

        Location entity = LocationRequest.Put.toEntity(dto);
        savedEntity.update(entity);
        Location rtnEntity = locationService.update(entity);
        return LocationResponse.FindById.toDto(rtnEntity);
    }
}
