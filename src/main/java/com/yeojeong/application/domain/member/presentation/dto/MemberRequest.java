package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class MemberRequest {

    public record FindPassword(
            @Size(min = 5, max = 30)
            String username,

            @Size(min = 1, max = 50) @Email
            String email
    ){}

    public record MemberPut(
            @Size(max = 10)
            @Schema(nullable = true)
            String nickname,

            @Min(0) @Max(120)
            @Schema(nullable = true)
            Integer age
    ) {
        public static Member toEntity(MemberPut dto) {
            return Member.builder()
                    .nickname(dto.nickname)
                    .age(dto.age)
                    .build();
        }
    }

    public record PatchPassword(
            @NotBlank @Size(min = 8, max = 30)
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                    message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
            String checkPassword,

            @NotBlank @Size(min = 8, max = 30)
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                    message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
            String password
    ) {}

    public record CheckPassword(
            @NotBlank @Size(min = 8, max = 30)
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                    message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
            String password
    ) {}

    public record EmailAuthedKey (
            @NotBlank
            @Pattern(regexp = "^\\d{4}$", message = "인증 코드는 4자리 숫자입니다.")
            String key
    ){}

    public record MemberSave(
        @NotBlank @Size(min = 5, max = 30)
        String username,

        @NotBlank @Size(min = 1, max = 10)
        String nickname,

        @NotBlank @Size(min = 1, max = 50)
        @Size(min = 1, max = 50) @Email
        String email,

        @NotBlank @Size(min = 8, max = 30)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
        String password,

        @NotNull
        @Min(1) @Max(120)
        Integer age
    ) {
        public static Member toEntity(MemberSave dto, String password) {
            if(dto.password.equals(password)) return null;
            return Member.builder()
                    .username(dto.username())
                    .nickname(dto.nickname())
                    .email(dto.email())
                    .age(dto.age())
                    .password(password)
                    .role(MemberRole.USER.getRole())
                    .lastLoginDate(LocalDate.now())
                    .build();
        }
    }
}
