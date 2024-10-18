package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import lombok.*;

import java.time.LocalDateTime;

public class BoardResponse {
    @Builder
    public record FindAll(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,

            MemberInfo member,

            Integer view,
            Integer avgScore,
            Integer commentCount
    ) {
        @Builder
        private record MemberInfo(
                Long id,
                String nickname
        ){}

        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ){}

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
                    .member(MemberInfo.builder()
                            .id(board.getMember().getId())
                            .nickname(board.getMember().getNickname())
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

            MemberInfo member
    ) {
        @Builder
        private record MemberInfo (
                Long userId,
                String nickname
        ){}

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
                    .member(MemberInfo.builder()
                            .userId(board.getMember().getId())
                            .nickname(board.getMember().getNickname())
                            .build())
                    .build();
        }
    }
}
