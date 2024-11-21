package com.yeojeong.application.domain.planner.location.application.locationfacade;

import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import org.springframework.data.domain.Page;

public interface LocationFacade {
    LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId);
    void delete(Long id);
    LocationResponse.FindById update(LocationRequest.Put dto, Long id);
}
