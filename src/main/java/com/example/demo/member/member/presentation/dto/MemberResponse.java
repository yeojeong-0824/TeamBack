package com.example.demo.member.member.presentation.dto;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class MemberResponse {
    @Builder
    @Schema(name = "유저 정보 호출")
    public record FindMember (
        @Schema(example = "user12")
        String username,

        @Schema(example = "소인국갔다옴")
        String nickname,

        @Schema(example = "example@naver.com")
        String email,

        @Schema(example = "걸리버")
        String name,

        @Schema(example = "90")
        Integer age
    ) {
        public static FindMember toDto(Member entity) {
            return FindMember.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .age(entity.getAge())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 게시글 정보 호출")
    public record BoardInfo(
            @Schema(example = "1")
            Long id,

            @Schema(example = "소인국 갔다온 썰 푼다")
            String title
    ){
        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 남긴 별점 정보 호출")
    public record BoardScoreInfo(
            @Schema(example = "1")
            Long boardId,

            @Schema(example = "소인국 갔다온 썰 푼다")
            String boardTitle,

            @Schema(example = "5")
            Integer score
    ){
        public static BoardScoreInfo toDto(BoardScore boardScore) {
            return BoardScoreInfo.builder()
                    .boardId(boardScore.getBoard().getId())
                    .boardTitle(boardScore.getBoard().getTitle())
                    .score(boardScore.getScore())
                    .build();
        }
    }
}
