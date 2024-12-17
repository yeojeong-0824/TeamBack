package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.utildto.UtilResponse;
import lombok.*;

import java.time.LocalDateTime;

public class BoardResponse {
    @Builder
    public record BoardInfo(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도

            String title,
            String body,  // Body 변수명을 소문자로 변경

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberResponse.MemberInfo memberInfo,
            UtilResponse.TimeInfo time
    ) {
        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .locationName(board.getLocationName())
                    .formattedAddress(board.getFormattedAddress())
                    .latitude(board.getLatitude())
                    .longitude(board.getLongitude())

                    .title(board.getTitle())
                    .body(board.getBody())

                    .view(board.getView())
                    .avgScore(board.getAvgScore())
                    .commentCount(board.getCommentCount())

                    .memberInfo(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    public record FindAll(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberResponse.MemberInfo member,
            UtilResponse.TimeInfo time
    ) {
        public static FindAll toDto(Board board) {
            return FindAll.builder()
                    .id(board.getId())
                    .locationName(board.getLocationName())
                    .formattedAddress(board.getFormattedAddress())
                    .latitude(board.getLatitude())
                    .longitude(board.getLongitude())
                    .title(board.getTitle())

                    .view(board.getView())
                    .avgScore(board.getAvgScore())
                    .commentCount(board.getCommentCount())
                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    public record FindById(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도

            String title,
            String body,  // Body 변수명을 소문자로 변경

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberResponse.MemberInfo member,
            Long planner,
            UtilResponse.TimeInfo time
    ) {
        public static FindById toDto(Board board) {
            return FindById.builder()
                    .id(board.getId())
                    .locationName(board.getLocationName())
                    .formattedAddress(board.getFormattedAddress())
                    .latitude(board.getLatitude())
                    .longitude(board.getLongitude())

                    .title(board.getTitle())
                    .body(board.getBody())

                    .view(board.getView())
                    .avgScore(board.getAvgScore())
                    .commentCount(board.getCommentCount())

                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .planner(board.getPlannerId())

                    .time(UtilResponse.TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }
}
