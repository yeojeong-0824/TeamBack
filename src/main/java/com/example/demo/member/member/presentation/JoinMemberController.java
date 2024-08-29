package com.example.demo.member.member.presentation;

import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.member.member.application.memberservice.MemberService;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.email.application.JoinMemberEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Validated
@Tag(name = "회원가입 API")
public class JoinMemberController {

    private final MemberService memberService;
    private final JoinMemberEmailService joinMemberEmailService;

    /*
    회원가입 작동 방식:
    1. 이메일 중복 검사 및 유효성 검사 후 인증 이메일 발송
    2. 인증 이메일 값 확인 후 인증이 완료 되었으면 인증된 이메일로 전환
    3. 아이디 및 닉네임 중복 검사 및 유효성 검사
    4. 회원가입(회원가입 시 이메일 인증을 진행하지 않았을 때는 인증되지 않은 이메일을 반환)
     */


    @MethodTimer(method = "회원가입 호출")
    @PostMapping
    @Operation(summary = "회원가입", description = "회원을 생성합니다.")
    @ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "유저 생성 완료"),
                @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
                @ApiResponse(responseCode = "401", description = "인증되지 않은 이메일")
        }
    )
    public ResponseEntity<String> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                       @Valid @RequestBody MemberRequest.SaveMember takenDto) {

        if(!joinMemberEmailService.checkAuthedEmail(takenDto.email()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 이메일입니다");

        memberService.save(takenDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("생성이 완료되었습니다");
    }


    @MethodTimer(method = "아이디 및 닉네임 중복 검사 호출")
    @PostMapping("/confirm")
    @Operation(summary = "아이디 및 닉네임 중복 검사", description = "아이디 및 닉네임이 이미 사용되고 있는지 확인합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "중복됨")
            }
    )
    public ResponseEntity<String> checkDuplicatedByDataConfirmMember(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                                     @Valid @RequestBody MemberRequest.DataConfirmMember takenDto) {

        memberService.checkDuplicated(takenDto);
        return ResponseEntity.ok("중복되지 않았습니다");
    }


    @MethodTimer(method = "이메일 중복 확인 및 회원가입 인증 이메일 발송 호출")
    @GetMapping("/emailAuthed/{email}")
    @Operation(summary = "이메일 중복 확인 및 회원가입 인증 이메일 발송", description = "이메일이 중복되었는지 확인 후 중복되지 않았으면 인증 이메일을 발송합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "인증 이메일 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "이메일이 중복 중복됨")
            }
    )
    public ResponseEntity<String> authedByEmail(@NotBlank @Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                                @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                         message = "유효한 이메일이 아닙니다.")
                                                @PathVariable("email") String email) {

        memberService.checkDuplicatedByEmail(email);

        String key = joinMemberEmailService.createAuthedKey();
        joinMemberEmailService.sendAuthedEmail(email, key);

        return ResponseEntity.ok("이메일 인증 코드 전송되었습니다");
    }


    @MethodTimer(method = "이메일 인증 확인 호출")
    @PostMapping("/emailAuthed/{email}")
    @Operation(summary = "이메일 인증코드 확인", description = "이메일 인증을 시도합니다. 이메일 인증 이메일이 발송되지 않았으면 이메일 인증 실패를 하게 됩니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
                    @ApiResponse(responseCode = "401", description = "이메일 인증 실패")
            }
    )
    public ResponseEntity<String> authedCheckByEmail(@Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                                     @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                              message = "유효한 이메일이 아닙니다.")
                                                     @PathVariable("email") String email,

                                                     @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                     @RequestBody MemberRequest.EmailAuthedKey takenDto) {

        return joinMemberEmailService.checkAuthedKey(email, takenDto.key()) ?
                ResponseEntity.ok("이메일 인증에 성공하였습니다") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 인증에 실패하였습니다");
    }
}
