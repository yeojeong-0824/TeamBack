package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponse {
    @Builder
    @Schema(name = "유저 정보 호출")
    public record FindById(
        String username,
        String nickname,
        String email,
        Integer age,
        TimeInfo time
    ) {
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}
        public static FindById toDto(Member entity) {
            return FindById.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .age(entity.getAge())
                    .time(TimeInfo.builder()
                            .createTime(entity.getCreateAt())
                            .updateTime(entity.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 게시글 정보 호출")
    public record BoardInfo(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberInfo member,
            TimeInfo time
    ){
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

        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
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
                    .time(TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 댓글 정보 호출")
    public record CommentInfo(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,

            Integer view,
            Integer avgScore,
            Integer commentCount,

            MemberInfo member,
            TimeInfo time
    ){
        @Builder
        private record MemberInfo(
                Long id,
                String nickname
        ){}

        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}

        public static CommentInfo toDto(Comment comment) {
            Board board = comment.getBoard();
            return CommentInfo.builder()
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
                    .time(TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    public record PlannerInfo(
            Long id,
            String title,
            String subTitle,
            int personnel,

            int locationCount
    ) {
        public static PlannerInfo toDto(Planner planner) {
            return PlannerInfo.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .subTitle(planner.getSubTitle())
                    .personnel(planner.getPersonnel())
                    .locationCount(planner.getLocationCount())
                    .build();
        }
    }

    @Builder
    public record patchKey(
            String key
    ) {}
}
