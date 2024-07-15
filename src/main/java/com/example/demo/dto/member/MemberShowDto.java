package com.example.demo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "유저 상세 데이터")
public class MemberShowDto {
    private String username;
    private String nickname;
    private String phoneNumber;
    private String name;
    private Integer age;
}
