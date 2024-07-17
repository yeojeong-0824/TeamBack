package com.example.demo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@Schema(name = "유저 회원가입 정보 입력") //DTO의 설명
public class MemberRequest {

    @NotBlank
    @Size(min = 5, max = 30)
    @Schema(example = "user12") // 들어갈 데이터 예시
    private String username;

    @NotBlank @Size(min = 1, max = 10)
    @Schema(example = "소인국갔다옴")
    private String nickname;

    @NotBlank @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", // 이메일 정규식
            message = "유효한 이메일이 아닙니다.")
    @Schema(example = "example@naver.com")
    private String email;

    @NotBlank @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
            message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
    @Schema(example = "1q2w3e4r")
    private String password;

    @NotBlank @Size(min = 1, max = 10)
    @Schema(example = "걸리버")
    private String name;

    @NotNull
    @Min(1) @Max(120)
    @Schema(example = "90")
    private Integer age;
}
