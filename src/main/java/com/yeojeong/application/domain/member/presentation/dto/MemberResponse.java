package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
            String title,
            TimeInfo time
    ){
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}

        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .title(board.getTitle())
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
            CommentBoardInfo boardInfo,
            Integer score,
            String comment,
            TimeInfo time
    ){
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}
        @Builder
        private record CommentBoardInfo(
                Long id,
                String title
        ){}
        public static CommentInfo toDto(Comment comment) {
            return CommentInfo.builder()
                    .boardInfo(CommentBoardInfo.builder()
                            .id(comment.getBoard().getId())
                            .title(comment.getBoard().getTitle())
                            .build())
                    .score(comment.getScore())
                    .comment(comment.getComment())
                    .time(TimeInfo.builder()
                            .createTime(comment.getCreateAt())
                            .updateTime(comment.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    public record PlannerInfo(
            Long id,
            String title,

            Integer startYear,
            Integer startMonth,
            Integer startDay,
            Integer startHour,
            Integer startMinute,

            Integer endYear,
            Integer endMonth,
            Integer endDay,
            Integer endHour,
            Integer endMinute,

            int locationCount,
            List<LocationResponse.FindById> locationInfo
    ) {
        public static PlannerResponse.FindById toDto(Planner planner, List<LocationResponse.FindById> locationInfo) {
            return PlannerResponse.FindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())

                    .startYear(planner.getStartYear())
                    .startMonth(planner.getStartMonth())
                    .startDay(planner.getStartDay())
                    .startHour(planner.getStartHour())
                    .startMinute(planner.getStartMinute())

                    .endYear(planner.getEndYear())
                    .endMonth(planner.getEndMonth())
                    .endDay(planner.getEndDay())
                    .endHour(planner.getEndHour())
                    .endMinute(planner.getEndMinute())

                    .locationCount(planner.getLocationCount())
                    .locationInfo(locationInfo)
                    .build();
        }
    }

    @Builder
    public record patchKey(
            String key
    ) {}
}
