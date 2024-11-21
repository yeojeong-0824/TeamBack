package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class LocationRequest {
    @Builder
    @Schema(name = "장소 작성")
    public record Save(
            @Min(0)
            Integer travelTime,

            @NotNull
            @Min(2024)
            Integer year,

            @NotNull
            @Min(1) @Max(12)
            Integer month,

            @NotNull
            @Min(1) @Max(31)
            Integer day,

            @NotNull
            @Min(1) @Max(24)
            Integer hour,

            @NotNull
            @Min(1) @Max(60)
            Integer minute,

            @NotBlank
            String place,

            @NotBlank
            String address,

            String memo
    ){
        public static Location toEntity (Save dto, Planner planner) {
            return Location.builder()
                    .travelTime(dto.travelTime())

                    .year(dto.year())
                    .month(dto.month())
                    .day(dto.day())

                    .hour(dto.hour())
                    .minute(dto.minute())

                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .build();
        }
    }

    @Builder
    @Schema(name = "장소 수정")
    public record Put(
            @Min(0)
            Integer travelTime,

            @NotNull
            @Min(2024)
            Integer year,

            @NotNull
            @Min(1) @Max(12)
            Integer month,

            @NotNull
            @Min(1) @Max(31)
            Integer day,

            @NotNull
            @Min(1) @Max(24)
            Integer hour,

            @NotNull
            @Min(1) @Max(60)
            Integer minute,

            @NotBlank
            String place,

            @NotBlank
            String address,

            String memo
    ){
        public static Location toEntity (Put dto) {
            return Location.builder()
                    .travelTime(dto.travelTime())

                    .year(dto.year())
                    .month(dto.month())
                    .day(dto.day())

                    .hour(dto.hour())
                    .minute(dto.minute())

                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .build();
        }
    }

}
