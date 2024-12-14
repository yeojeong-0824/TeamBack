package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class LocationRequest {
    @Builder
    @Schema(name = "장소 작성")
    public record Save(
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
        public static Location toEntity (Save dto, Planner planner) {
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
                    .build();
        }
    }

    @Builder
    @Schema(name = "장소 수정")
    public record Put(
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

    }

}
