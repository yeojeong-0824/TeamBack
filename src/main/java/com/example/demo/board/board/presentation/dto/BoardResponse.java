package com.example.demo.board.board.presentation.dto;

import com.example.demo.board.board.domain.Board;
import lombok.*;

public class BoardResponse {
    @Builder
    public record FindBoardList(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,

            Integer view,
            Integer avgScore,
            Integer commentCount
    ) {
        public static FindBoardList toDto(Board board) {
            return FindBoardList.builder()
                    .id(board.getId())
                    .locationName(board.getLocationName())
                    .formattedAddress(board.getFormattedAddress())
                    .latitude(board.getLatitude())
                    .longitude(board.getLongitude())
                    .title(board.getTitle())

                    .view(board.getView())
                    .avgScore(board.getAvgScore())
                    .commentCount(board.getCommentCount())
                    .build();
        }
    }

    @Builder
    public record FindBoard(
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

        public static FindBoard toDto(Board board) {
            return FindBoard.builder()
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
