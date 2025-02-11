package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import com.yeojeong.application.domain.utildto.UtilResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


public class MemberResponse {
    @Builder
    public record MemberInfo(
            String username,
            String nickname,
            String email,
            Integer age
    ) {
        public static MemberInfo toDto(Member member) {
            return MemberInfo.builder()
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .age(member.getAge())
                    .build();
        }
    }

    @Builder
    public record FindById(
        String username,
        String nickname,
        String email,
        Integer age,
        UtilResponse.TimeInfo time
    ) {
        public static FindById toDto(Member member) {
            return FindById.builder()
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .age(member.getAge())
                    .time(UtilResponse.TimeInfo.toDto(member))
                    .build();
        }
    }

    @Builder
    public record MemberBoardInfo(
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
    ){
        public static MemberBoardInfo toDto(Board board) {
            return MemberBoardInfo.builder()
                    .id(board.getId())
                    .locationName(board.getLocationName())
                    .formattedAddress(board.getFormattedAddress())
                    .latitude(board.getLatitude())
                    .longitude(board.getLongitude())
                    .title(board.getTitle())

                    .view(board.getView())
                    .avgScore(board.getAvgScore())
                    .commentCount(board.getCommentCount())
                    .member(MemberInfo.toDto(board.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(board))
                    .build();
        }
    }

    @Builder
    public record MemberCommentInfo(
            BoardResponse.BoardInfo board,
            List<CommentResponse.CommentInfo> commentList
    ){
        public static MemberCommentInfo createMemberCommentInfo(Comment comment) {
            List<CommentResponse.CommentInfo> commentDataList = new ArrayList<>();
            commentDataList.add(CommentResponse.CommentInfo.toDto(comment));

            return MemberCommentInfo.builder()
                    .board(BoardResponse.BoardInfo.toDto(comment.getBoard()))
                    .commentList(commentDataList)
                    .build();
        }

        public void appendComment(Comment comment) {
            this.commentList.add(CommentResponse.CommentInfo.toDto(comment));
        }
    }

    @Builder
    public record MemberPlannerInfo(
            Long id,
            String title,
            String subTitle,
            int personnel,
            int locationCount
    ) {
        public static MemberPlannerInfo toDto(Planner planner) {
            return MemberPlannerInfo.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .subTitle(planner.getSubTitle())
                    .personnel(planner.getPersonnel())
                    .locationCount(planner.getLocationCount())
                    .build();
        }
    }

    @Builder
    public record MemberLocationInfo(
            PlannerResponse.PlannerInfo planner
    ) {
        public static MemberLocationInfo toDto(Location location) {
            return MemberLocationInfo.builder()
                    .planner(PlannerResponse.PlannerInfo.toDto(location.getPlanner()))
                    .build();
        }
    }
}
