package com.example.demo.member.member.presentation.dto;

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
        static public FindMember toDto(Member entity) {
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
    @Schema(name = "자세한 유저 정보 호출")
    public record FindMemberDetail (
            @Schema(example = "user12")
            String username,

            @Schema(example = "소인국갔다옴")
            String nickname,

            @Schema(example = "example@naver.com")
            String email,

            @Schema(example = "걸리버")
            String name,

            @Schema(example = "90")
            Integer age,

            List<BoardInfo> board,
            List<BoardScoreInfo> boardScore
    ) {
        @Builder
        private record BoardInfo(
                @Schema(example = "1")
                Long id,

                @Schema(example = "소인국 갔다온 썰 푼다")
                String title
        ){}

        @Builder
        private record BoardScoreInfo(
                @Schema(example = "소인국 갔다온 썰 푼다")
                String boardTitle,

                @Schema(example = "1")
                Long boardId,

                @Schema(example = "5")
                Integer score
        ){}

        static public FindMemberDetail toDto(Member entity) {

            List<BoardInfo> board = entity.getBoard().stream().map(data ->
                BoardInfo.builder()
                        .id(data.getId())
                        .title(data.getTitle())
                        .build()
            ).toList();

            List<BoardScoreInfo> boardScore = entity.getBoardScore().stream().map(data ->
                BoardScoreInfo.builder()
                        .boardTitle(data.getBoard().getTitle())
                        .boardId(data.getBoard().getId())
                        .score(data.getScore())
                        .build()
            ).toList();

            return FindMemberDetail.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .age(entity.getAge())
                    .board(board)
                    .boardScore(boardScore)
                    .build();
        }
    }
}
