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
    private String username;

    @NotBlank @Size(min = 1, max = 10)
    private String nickname;

    //Size를 11자리로 맞춰야 함
    @NotBlank @Size(min = 1, max = 100)
    private String phoneNumber;

    @NotBlank @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$",
            message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
    @Schema(allowableValues = "string") // 파라미터를 받는 예시 값(?) 같은 건데 프론트에서 보기 편하게 자료형으로 표현함
    private String password;

    @NotBlank @Size(min = 1, max = 10)
    private String name;

    @NotNull
    @Min(1) @Max(100)
    private Integer age;
}
