package com.example.demo.member.member.presentation.dto;

import com.example.demo.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
        static public FindMember toFindMember(Member entity) {
            return FindMember.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .age(entity.getAge())
                    .build();
        }
    }
}
