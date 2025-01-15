package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.utildto.UtilResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponse {
    public static int getAvgScore(List<Comment> commentList) {
        int commentCount = commentList.size();
        if(commentCount == 0) return 0;

        int size = 0;
        int sum = 0;

        for(Comment comment : commentList) {
            int score = comment.getScore();
            if(comment.getScore() == 0) continue;

            size++;
            sum += score;
        }
        if(size == 0) return 0;
        return (sum * 100) / size;
    }

    @Builder
    public record BoardInfo(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도

            String title,
            String body,

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberResponse.MemberInfo member,
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
                    .avgScore(BoardResponse.getAvgScore(board.getComments()))
                    .commentCount(board.getComments().size())

                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(board))
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
                    .avgScore(BoardResponse.getAvgScore(board.getComments()))
                    .commentCount(board.getComments().size())
                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(board))
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
                    .avgScore(BoardResponse.getAvgScore(board.getComments()))
                    .commentCount(board.getComments().size())

                    .member(MemberResponse.MemberInfo.toDto(board.getMember()))
                    .planner(board.getPlannerId())

                    .time(UtilResponse.TimeInfo.toDto(board))
                    .build();
        }
    }
}
