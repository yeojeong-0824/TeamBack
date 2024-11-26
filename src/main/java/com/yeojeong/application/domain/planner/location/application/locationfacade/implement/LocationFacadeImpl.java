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

@Service
@RequiredArgsConstructor
public class LocationFacadeImpl implements LocationFacade {

    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    public LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId) {
        Planner planner = plannerService.findById(plannerId);

        Location entity = LocationRequest.Save.toEntity(dto, planner);

        if (startDateValidation(planner, entity)){
            throw new RequestDataException("Planner (시작 날짜) 에 해당하지 않는 날짜입니다.");
        }
        if (endDateValidation(planner, entity)) {
            throw new RequestDataException("Planner (끝 날짜) 에 해당하지 않는 날짜입니다.");
        }

        Location savedEntity = locationService.save(entity);

        if (planner.getLocationCount() >= 15) {
            throw new RequestDataException("Location 은 15개까지 생성 가능합니다.");
        }

        planner.addLocation();
        plannerService.save(planner);

        return LocationResponse.FindById.toDto(savedEntity);
    }

    @Override
    public void delete(Long id) {
        Location savedEntity = locationService.findById(id);
        Planner planner = plannerService.findById(savedEntity.getPlanner().getId());

        locationService.delete(savedEntity);

        planner.deleteLocation();
        plannerService.save(planner);

    }

    @Override
    public LocationResponse.FindById update(LocationRequest.Put dto, Long id) {
        Location savedEntity = locationService.findById(id);
        Location entity = LocationRequest.Put.toEntity(dto);

        Planner planner = savedEntity.getPlanner();

        if (startDateValidation(planner, entity)){
            throw new RequestDataException("Planner (시작 날짜) 에 해당하지 않는 날짜입니다.");
        }
        if (endDateValidation(planner, entity)) {
            throw new RequestDataException("Planner (끝 날짜) 에 해당하지 않는 날짜입니다.");
        }

        savedEntity.update(entity);
        Location rtnEntity = locationService.update(entity);
        return LocationResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public LocationResponse.FindById findById(Long id) {
        locationService.findById(id);
        return LocationResponse.FindById.toDto(locationService.findById(id));
    }

    public boolean startDateValidation (Planner planner, Location location) {
        if (location.getYear() < planner.getStartYear()){
            return true;
        } else if (location.getYear() == planner.getStartYear() && location.getMonth() < planner.getStartMonth()) {
            return true;
        } else if (location.getYear() == planner.getStartYear() && location.getMonth() == planner.getStartMonth() && location.getDay() < planner.getStartDay()) {
            return true;
        } else if (location.getYear() == planner.getStartYear() && location.getMonth() == planner.getStartMonth() && location.getDay() == planner.getStartDay() && location.getHour() < planner.getStartHour()) {
            return true;
        } else if (location.getYear() == planner.getStartYear() && location.getMonth() == planner.getStartMonth() && location.getDay() == planner.getStartDay() && location.getHour() == planner.getStartHour() && location.getMinute() < planner.getStartMinute()) {
            return true;
        } return false;
    }

    public boolean endDateValidation (Planner planner, Location location) {
        if (location.getYear() > planner.getEndYear()){
            return true;
        } else if (location.getYear() == planner.getEndYear() && location.getMonth() > planner.getEndMonth()) {
            return true;
        } else if (location.getYear() == planner.getEndYear() && location.getMonth() == planner.getEndMonth() && location.getDay() > planner.getEndDay()) {
            return true;
        } else if (location.getYear() == planner.getEndYear() && location.getMonth() == planner.getEndMonth() && location.getDay() == planner.getEndDay() && location.getHour() > planner.getEndHour()) {
            return true;
        } else if (location.getYear() == planner.getEndYear() && location.getMonth() == planner.getEndMonth() && location.getDay() == planner.getEndDay() && location.getHour() == planner.getEndHour() && location.getMinute() > planner.getEndMinute()) {
            return true;
        } return false;
    }
}
