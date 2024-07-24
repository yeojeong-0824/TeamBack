package com.example.demo.controller.member;

import com.example.demo.service.email.EmailService;
import com.example.demo.service.member.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "인증된 유저 API")
public class AuthedMemberController {

    private final MemberService memberService;
    private final EmailService emailService;
}
