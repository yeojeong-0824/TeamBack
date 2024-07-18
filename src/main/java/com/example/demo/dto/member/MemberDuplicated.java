package com.example.demo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@Schema(name = "유저 데이터 중복 확인")
public class MemberDuplicated {
    @Size(min = 5, max = 30)
    @Schema(example = "user12", nullable = true) // 들어갈 데이터 예시
    private String username;

    @Size(min = 1, max = 10)
    @Schema(example = "소인국갔다옴", nullable = true)
    private String nickname;

    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", // 이메일 정규식
            message = "유효한 이메일이 아닙니다.")
    @Schema(example = "example@naver.com", nullable = true)
    private String email;
}
