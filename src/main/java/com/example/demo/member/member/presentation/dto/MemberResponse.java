package com.example.demo.member.member.presentation.dto;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            List<String> boardTitle,
            Map<String, Integer> boardScore
    ) {
        static public FindMemberDetail toDto(Member entity) {
            Map<String, Integer> boardScore = new HashMap<>();

            for(BoardScore data : entity.getBoardScore()) {
                boardScore.put(data.getBoard().getTitle(), data.getScore());
            }

            return FindMemberDetail.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .age(entity.getAge())
                    .boardTitle(entity.getBoard().stream().map(Board::getTitle).toList())
                    .boardScore(boardScore)
                    .build();
        }
    }
}
