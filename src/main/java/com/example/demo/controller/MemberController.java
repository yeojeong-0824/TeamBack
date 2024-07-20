package com.example.demo.controller;

import com.example.demo.dto.member.MemberRequest.*;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.service.EmailService;
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
import jakarta.validation.constraints.NotNull;
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
                @ApiResponse(responseCode = "400", description = "유효성 검사 실패"), // 해당 메소드가 반환하는 Http Status 코드의 대한 설명
                @ApiResponse(responseCode = "401", description = "인증되지 않은 이메일")
        }
    )
    public ResponseEntity<String> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) // 메소드가 받는 파라미터는 Json 형식을 사용한다
                                       @Valid @RequestBody CreateMember takenDto,
                                       HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        log.info("{}: 유저 생성 API 호출", ip);

        if(!emailService.checkAuthedEmail(takenDto.email()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 이메일");

        memberService.addUser(takenDto);
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

        return ResponseEntity.ok("중복되지 않음");
    }

    @GetMapping("/emailAuthed/{email}")
    @Operation(summary = "이메일 인증코드 전송")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "인증 이메일 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨")
            }
    )
    public ResponseEntity<String> emailAuthed(@NotBlank @Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                              @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                       message = "유효한 이메일이 아닙니다.")
                                              @PathVariable("email") String email,
                                              HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        log.info("{}: 이메일 인증코드 전송 API 호출", ip);

        if(!memberService.checkDuplicatedEmail(email))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일이 중복되었습니다");

        String authendKey = emailService.createAuthedKey();
        String title = "이메일 인증 코드";
        String text = "이메일 인증 코드: " + authendKey;

        emailService.sendEmail(email, title, text, authendKey);
        return ResponseEntity.ok("이메일 인증 코드 전송");
    }

    @PostMapping("/emailAuthed/{email}")
    @Operation(summary = "이메일 인증코드 확인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
                    @ApiResponse(responseCode = "401", description = "이메일 인증 실패")
            }
    )
    public ResponseEntity<String> emailAuthedCheck(@NotBlank @Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                                   @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                            message = "유효한 이메일이 아닙니다.")
                                                   @PathVariable("email") String email,

                                                   @NotBlank @Schema(example = "1234")
                                                   @Pattern(regexp = "^\\d{4}$", message = "인증 코드는 4자리 숫자입니다.")
                                                   @RequestBody String key,

                                                   HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        log.info("{}: 이메일 인증 API 호출", ip);

        return emailService.checkAuthedKey(email, key) ?
                ResponseEntity.ok("이메일 인증 성공") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 인증 실패");
    }
}
