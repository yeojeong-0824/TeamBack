package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LocationRequest {
    public record LocationSave(
            @NotNull
            Long unixTime,

            @Min(0)
            Integer travelTime,

            String transportation,
            String transportationNote,

            @NotBlank
            String place,

            @NotBlank
            String address,

            String phoneNumber,

            String memo
    ){
        public static Location toEntity (LocationSave dto, Planner planner, Member member) {
            return Location.builder()
                    .unixTime(dto.unixTime())
                    .travelTime(dto.travelTime())

                    .transportation(dto.transportation())
                    .transportationNote(dto.transportationNote())

                    .place(dto.place())
                    .address(dto.address())
                    .phoneNumber(dto.phoneNumber())
                    .memo(dto.memo())

                    .planner(planner)
                    .member(member)
                    .build();
        }
    }

    public record LocationPut(
            @NotNull
            Long unixTime,

            @Min(0)
            Integer travelTime,

            String transportation,
            String transportationNote,

            @NotBlank
            String place,
            @NotBlank
            String address,

            String phoneNumber,

            String memo
    ){
        public static Location toEntity (LocationPut dto) {
            return Location.builder()
                    .unixTime(dto.unixTime())
                    .travelTime(dto.travelTime())

                    .transportation(dto.transportation())
                    .transportationNote(dto.transportationNote())

                    .place(dto.place())
                    .address(dto.address())
                    .phoneNumber(dto.phoneNumber())
                    .memo(dto.memo())

                    .build();
        }
    }

}
