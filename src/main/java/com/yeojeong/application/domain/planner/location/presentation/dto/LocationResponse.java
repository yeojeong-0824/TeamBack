package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.utildto.UtilResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class LocationResponse {

    @Builder
    public record LocationInfo(
            Long id,
            Long unixTime,
            Integer travelTime,

            String transportation,
            String transportationNote,

            String place,
            String address,
            String phoneNumber,

            String memo,
            Long plannerId,

            UtilResponse.TimeInfo time
    ){
        public static LocationInfo toDto(Location location) {
            return LocationInfo.builder()
                    .id(location.getId())

                    .unixTime(location.getUnixTime())
                    .travelTime(location.getTravelTime())

                    .transportation(location.getTransportation())
                    .transportationNote(location.getTransportationNote())

                    .place(location.getPlace())
                    .address(location.getAddress())
                    .phoneNumber(location.getPhoneNumber())

                    .memo(location.getMemo())
                    .plannerId(location.getPlanner().getId())

                    .time(UtilResponse.TimeInfo.toDto(location))
                    .build();
        }
    }

    @Builder
    public record LocationInfo(
            Long id,
            Long unixTime,
            Integer travelTime,

            String transportation,
            String transportationNote,

            String place,
            String address,
            String phoneNumber,

            String memo,
            Long plannerId,

            UtilResponse.TimeInfo time
    ) {
        public static LocationInfo toDto(Location location) {
            return LocationInfo.builder()
                    .id(location.getId())

                    .unixTime(location.getUnixTime())
                    .travelTime(location.getTravelTime())

                    .transportation(location.getTransportation())
                    .transportationNote(location.getTransportationNote())

                    .place(location.getPlace())
                    .address(location.getAddress())
                    .phoneNumber(location.getPhoneNumber())

                    .memo(location.getMemo())
                    .plannerId(location.getPlanner().getId())

                    .time(UtilResponse.TimeInfo.toDto(location))
                    .build();
        }
    }

    @Builder
    @Schema(name = "장소 조회")
    public record FindById(
            Long id,
            Long unixTime,
            Integer travelTime,

            String transportation,
            String transportationNote,

            String place,
            String address,
            String phoneNumber,

            String memo,
            Long plannerId,

            UtilResponse.TimeInfo time
    ) {
        public static FindById toDto(Location location) {
            return FindById.builder()
                    .id(location.getId())

                    .unixTime(location.getUnixTime())
                    .travelTime(location.getTravelTime())

                    .transportation(location.getTransportation())
                    .transportationNote(location.getTransportationNote())

                    .place(location.getPlace())
                    .address(location.getAddress())
                    .phoneNumber(location.getPhoneNumber())

                    .memo(location.getMemo())
                    .plannerId(location.getPlanner().getId())

                    .time(UtilResponse.TimeInfo.toDto(location))
                    .build();
        }
    }
}
