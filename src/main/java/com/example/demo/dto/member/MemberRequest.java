package com.example.demo.dto.member;

import com.example.demo.config.exception.ServerException;
import com.example.demo.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

public class MemberRequest {
    @Builder
    @Schema(name = "유저 중복 검사")
    public record DataConfirmMember(
            @Size(min = 5, max = 30)
            @Schema(example = "user12", nullable = true) // 들어갈 데이터 예시
            String username,

            @Size(min = 1, max = 10)
            @Schema(example = "소인국갔다옴", nullable = true)
            String nickname,

            @Size(min = 1, max = 50)
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", // 이메일 정규식
                    message = "유효한 이메일이 아닙니다.")
            @Schema(example = "example@naver.com", nullable = true)
            String email
    ) {}

    @Builder
    @Schema(name = "유저 회원가입 정보 입력")
    public record CreateMember (
        @NotBlank @Size(min = 5, max = 30)
        @Schema(example = "user12") // 들어갈 데이터 예시
        String username,

        @NotBlank @Size(min = 1, max = 10)
        @Schema(example = "소인국갔다옴")
        String nickname,

        @NotBlank @Size(min = 1, max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", // 이메일 정규식
                message = "유효한 이메일이 아닙니다.")
        @Schema(example = "example@naver.com")
        String email,

        @NotBlank @Size(min = 8, max = 30)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
        @Schema(example = "1q2w3e4r")
        String password,

        @NotBlank @Size(min = 1, max = 10)
        @Schema(example = "걸리버")
        String name,

        @NotNull
        @Min(1) @Max(120)
        @Schema(example = "90")
        Integer age
    ) {
        static public Member toEntity(CreateMember dto, String password) {
            if(dto.password.equals(password)) throw new ServerException("비밀번호 암호화가 진행되지 않았습니다");
            return Member.builder()
                    .username(dto.username())
                    .nickname(dto.nickname())
                    .email(dto.email())
                    .name(dto.name())
                    .age(dto.age())
                    .password(password)
                    .role(MemberRole.USER.getRole())
                    .build();
        }
    }
}
