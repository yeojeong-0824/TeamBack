package com.yeojeong.application.domain.board.comment.presentation.dto;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.utildto.UtilResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class CommentResponse {
    @Builder
    public record CommentInfo(
            Long id,
            Integer score,
            String comment,

            MemberResponse.MemberInfo member,
            UtilResponse.TimeInfo time
    ) {
        public static CommentInfo toDto(Comment comment) {
            return CommentInfo.builder()
                    .id(comment.getId())
                    .score(comment.getScore())
                    .comment(comment.getComment())
                    .member(MemberResponse.MemberInfo.toDto(comment.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(comment))
                    .build();
        }
    }

    @Builder
    public record FindByBoardId(
            Long id,
            Integer score,
            String comment,

            MemberResponse.MemberInfo member,
            UtilResponse.TimeInfo time
    ) {
        public static FindByBoardId toDto(Comment comment) {

            return FindByBoardId.builder()
                    .id(comment.getId())
                    .score(comment.getScore())
                    .comment(comment.getComment())
                    .member(MemberResponse.MemberInfo.toDto(comment.getMember()))
                    .time(UtilResponse.TimeInfo.toDto(comment))
                    .build();
        }
    }
}
