package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.utildto.UtilResponse;
import lombok.*;

import java.util.List;

public class BoardResponse {
    @Builder
    public record BoardInfo(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,
            String longitude,

            String title,
            String body,

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberResponse.MemberInfo member,
            UtilResponse.TimeInfo time,
            String image
    ) {
        public static BoardInfo toDto(Board board) {
            String image = null;
            if(!board.getImages().isEmpty()) {
                image = board.getImages().get(0);
            }
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

                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(board))
                    .image(image)
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
            UtilResponse.TimeInfo time,

            String image
    ) {
        public static FindAll toDto(Board board) {
            String image = null;
            if(!board.getImages().isEmpty()) {
                image = board.getImages().get(0);
            }
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
                    .time(UtilResponse.TimeInfo.toDto(board))

                    .image(image)
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
            UtilResponse.TimeInfo time,

            List<String> images
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

                    .time(UtilResponse.TimeInfo.toDto(board))
                    .images(board.getImages())
                    .build();
        }
    }

    @Builder
    public record ImageUrl(
            String url
    ) {
        public static ImageUrl toDto(String url) {
            return ImageUrl.builder().url(url).build();
        }
    }
}
