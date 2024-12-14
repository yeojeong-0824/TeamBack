package com.yeojeong.application.domain.planner.location.application.locationfacade;

import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;

import java.util.List;

public interface LocationFacade {
    LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId, Long memberId);
    void delete(Long id, Long memberId);
    LocationResponse.FindById update(LocationRequest.Put dto, Long id, Long memberId);
    LocationResponse.FindById findById(Long id, Long memberId);
    List<LocationResponse.FindById> findByPlannerId(Long plannerId, Long memberId);
}
