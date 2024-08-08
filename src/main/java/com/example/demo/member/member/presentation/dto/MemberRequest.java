package com.example.demo.member.member.presentation.dto;

import com.example.demo.config.exception.ServerException;
import com.example.demo.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

public class MemberRequest {

    @Schema(name = "비밀번호 수정")
    public record patchPassword (
            @NotBlank @Size(min = 8, max = 30)
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                    message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
            @Schema(example = "1q2w3e4r")
            String password
    ) {}

    @Schema(name = "닉네임 수정")
    public record patchNickname (
            @NotBlank @Size(min = 1, max = 10)
            @Schema(example = "소인국갔다옴")
            String nickname
    ) {}

    @Schema(name = "이메일 인증 코드")
    public record EmailAuthedKey (
            @NotBlank @Schema(example = "1234")
            @Pattern(regexp = "^\\d{4}$", message = "인증 코드는 4자리 숫자입니다.")
            String key
    ){}

    @Builder
    @Schema(name = "유저 중복 검사")
    public record DataConfirmMember (
            @Size(min = 5, max = 30)
            @Schema(example = "user12", nullable = true) // 들어갈 데이터 예시
            String username,

            @Size(min = 1, max = 10)
            @Schema(example = "소인국갔다옴", nullable = true)
            String nickname
    ) {}

    @Builder
    @Schema(name = "유저 회원가입 정보 입력")
    public record SaveMember(
        @NotBlank @Size(min = 5, max = 30)
        @Schema(example = "user12") // 들어갈 데이터 예시
        String username,

        @NotBlank @Size(min = 1, max = 10)
        @Schema(example = "소인국갔다옴")
        String nickname,

        @NotBlank @Size(min = 1, max = 50)
        @Email(message = "유효한 이메일이 아닙니다.")
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
        static public Member toEntity(SaveMember dto, String password) {
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
