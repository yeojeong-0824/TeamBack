package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.utildto.UtilResponse;
import lombok.Builder;

import java.util.List;

public class PlannerResponse {

    @Builder
    public record FindById(
            Long id,
            String title,
            int personnel,
            String subTitle,
            int locationCount,

            List<LocationResponse.FindById> locationInfo,
            UtilResponse.TimeInfo time
    ) {
        public static FindById toDto(Planner planner, List<LocationResponse.FindById> locationInfo) {
            return FindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .personnel(planner.getPersonnel())
                    .subTitle(planner.getSubTitle())

                    .locationCount(planner.getLocationCount())
                    .locationInfo(locationInfo)
                    .time(UtilResponse.TimeInfo.toDto(planner))
                    .build();
        }
    }
}
