package com.example.demo.controller;

import com.example.demo.dto.member.MemberRequest.*;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.security.email.EmailService;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Validated
@Tag(name = "유저 API") // 해당 클래스의 역할을 설명
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping
    @Operation(summary = "유저 생성") // 해당 메소드의 역할을 설명
    @ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "유저 생성 완료"), // 해당 메소드가 반환하는 Http Status 코드의 대한 설명
                @ApiResponse(responseCode = "400", description = "유효성 검사 실패") // 해당 메소드가 반환하는 Http Status 코드의 대한 설명
        }
    )
    public ResponseEntity<String> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) // 메소드가 받는 파라미터는 Json 형식을 사용한다
                                       @Valid @RequestBody CreateMember taken,
                                       HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 유저 생성 API 호출", ip);
        memberService.addUser(taken);
        return ResponseEntity.status(HttpStatus.CREATED).body("생성 완료");
    }

    @GetMapping
    @Operation(summary = "유저 목록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 목록 호출 성공")
            }
    )
    public ResponseEntity<List<MemberResponse>> findAll(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 유저 목록 API 호출", ip);
        List<MemberResponse> memberSaveDtoList = memberService.findAll();
        return ResponseEntity.ok(memberSaveDtoList);
    }

    @PostMapping("/confirm")
    @Operation(summary = "중복 검사")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "중복됨")
            }
    )
    public ResponseEntity<String> checkDuplicated(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                  @Valid @RequestBody DataConfirmMember taken,
                                                  HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        log.info("{}: 중복 검사 API 호출", ip);

        if(taken.username() != null && !memberService.checkDuplicatedUsername(taken.username()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디가 중복됨");

        if(taken.nickname() != null && !memberService.checkDuplicatedNickname(taken.nickname()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("닉네임이 중복됨");

        if(taken.email() != null && !memberService.checkDuplicatedEmail(taken.email()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일이 중복됨");

        return ResponseEntity.ok("중복되지 않음");
    }

    @PostMapping("/emailAuthed")
    @Operation(summary = "이메일 인증")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "중복됨")
            }
    )
    public ResponseEntity<String> emailAuthed(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                              @NotBlank @Size(min = 1, max = 50)
                                              @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일이 아닙니다.")
                                              @Schema(example = "example@naver.com")
                                              @RequestBody String email,
                                              HttpServletRequest request) {

        // ToDo: 이메일 인증 코드 생성 및 인증 절차

        String ip = request.getRemoteAddr();
        String title = "이메일 인증 코드";
        String text = "이메일 인증 코드";

//        emailService.sendEmail(email, title, text);

        return ResponseEntity.ok("중복되지 않음");
    }
}
