package com.example.demo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@Schema(name = "유저 상세 데이터")
public class MemberResponse {
    @Schema(example = "user12") // 들어갈 데이터 예시
    private String username;

    @Schema(example = "소인국갔다옴")
    private String nickname;

    @Schema(example = "example@naver.com")
    private String email;

    @Schema(example = "걸리버")
    private String name;

    @Schema(example = "90")
    private Integer age;
}
