package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.utildto.UtilResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class LocationResponse {
    @Builder
    @Schema(name = "장소 조회")
    public record FindById(
            Long id,
            Integer travelTime,
            Long unixTime,
            String place,
            String address,
            String memo,
            Long plannerId,

            UtilResponse.TimeInfo time
    ) {
        public static FindById toDto(Location location) {
            return FindById.builder()
                    .id(location.getId())

                    .travelTime(location.getTravelTime())
                    .unixTime(location.getUnixTime())

                    .place(location.getPlace())
                    .address(location.getAddress())
                    .memo(location.getMemo())
                    .plannerId(location.getPlanner().getId())

                    .time(UtilResponse.TimeInfo.builder()
                            .createTime(location.getCreateAt())
                            .updateTime(location.getUpdateAt())
                            .build())
                    .build();
        }
    }
}
