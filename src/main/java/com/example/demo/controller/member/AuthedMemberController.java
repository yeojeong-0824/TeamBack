package com.example.demo.controller.member;

import com.example.demo.dto.member.MemberDetails;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/authed")
@Validated
@Tag(name = "인증된 유저 API") // 해당 클래스의 역할을 설명
public class AuthedMemberController {

    private final MemberService memberService;
    private final EmailService emailService;
}
