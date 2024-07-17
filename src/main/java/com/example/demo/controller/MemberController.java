package com.example.demo.controller;

import com.example.demo.dto.member.MemberRequest;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Validated
@Tag(name = "유저 API") // 해당 클래스의 역할을 설명
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "유저 생성") // 해당 메소드의 역할을 설명
    @ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "유저 생성 완료"), // 해당 메소드가 반환하는 Http Status 코드의 대한 설명
                @ApiResponse(responseCode = "400", description = "유효성 검사 실패") // 해당 메소드가 반환하는 Http Status 코드의 대한 설명
        }
    )
    public ResponseEntity<String> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) // 메소드가 받는 파라미터는 Json 형식을 사용한다
                                       @Valid @RequestBody MemberRequest memberRequest) {
        memberService.addUser(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("생성 완료");
    }

    @GetMapping
    @Operation(summary = "유저 목록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 목록 호출 성공")
            }
    )
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> memberSaveDtoList = memberService.findAll();
        return ResponseEntity.ok(memberSaveDtoList);
    }


    @GetMapping("/username/{username}")
    @Operation(summary = "아이디 중복 검사")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "아이디가 중복됨")
            }
    )
    public ResponseEntity<String> checkDuplicatedUsername(@PathVariable("username")
                                                          @Size(min = 5, max = 30)
                                                          String username) {
        return memberService.checkDuplicatedUsername(username) ?
            ResponseEntity.ok("중복되지 않음") : ResponseEntity.status(HttpStatus.CONFLICT).body("아이디가 중복됨");
    }

    @GetMapping("/nickname/{nickname}")
    @Operation(summary = "닉네임 중복 검사")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "닉네임이 중복됨")
            }
    )
    public ResponseEntity<String> checkDuplicatedNickname(@PathVariable("nickname")
                                                          @Size(min = 1, max = 10)
                                                          String nickname) {
        return memberService.checkDuplicatedNickname(nickname) ?
                ResponseEntity.ok("중복되지 않음") : ResponseEntity.status(HttpStatus.CONFLICT).body("닉네임이 중복됨");
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "이메일 중복 검사")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "중복되지 않음"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
                    @ApiResponse(responseCode = "409", description = "이메일이 중복됨")
            }
    )
    public ResponseEntity<String> checkDuplicatedEmail(@PathVariable("email")
                                                       @NotBlank @Size(min = 1, max = 50)
                                                       @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                                message = "이메일이 유효하지 않습니다.")
                                                       String email) {
        return memberService.checkDuplicatedEmail(email) ?
                ResponseEntity.ok("중복되지 않음") : ResponseEntity.status(HttpStatus.CONFLICT).body("이메일이 중복됨");
    }
}
