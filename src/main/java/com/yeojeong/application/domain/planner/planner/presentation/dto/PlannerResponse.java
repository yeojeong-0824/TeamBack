package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.utildto.UtilResponse;
import lombok.Builder;

import java.util.List;

public class PlannerResponse {

    @Builder
    public record PlannerInfo(
            Long id,
            String title,
            int personnel,
            String subTitle,
            int locationCount,

            List<LocationResponse.LocationInfo> location,
            UtilResponse.TimeInfo time
    ) {
        public static PlannerInfo toDto(Planner planner) {
            int locationCount = planner.getLocations().size();
            List<Location> locations = planner.getLocations();
            List<LocationResponse.LocationInfo> locationInfos = locations.stream().map(LocationResponse.LocationInfo::toDto).toList();
            return PlannerInfo.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .personnel(planner.getPersonnel())
                    .subTitle(planner.getSubTitle())

                    .locationCount(locationCount)
                    .location(locationInfos)
                    .time(UtilResponse.TimeInfo.toDto(planner))
                    .build();
        }
    }

    @Builder
    public record PlannerFindById(
            Long id,
            String title,
            int personnel,
            String subTitle,
            int locationCount,

            List<LocationResponse.LocationInfo> location,
            UtilResponse.TimeInfo time
    ) {
        public static PlannerFindById toDto(Planner planner) {
            int locationCount = planner.getLocations().size();
            List<Location> locations = planner.getLocations();
            List<LocationResponse.LocationInfo> locationInfos = locations.stream().map(LocationResponse.LocationInfo::toDto).toList();
            return PlannerFindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .personnel(planner.getPersonnel())
                    .subTitle(planner.getSubTitle())

                    .locationCount(locationCount)
                    .location(locationInfos)
                    .time(UtilResponse.TimeInfo.toDto(planner))
                    .build();
        }
    }
}
