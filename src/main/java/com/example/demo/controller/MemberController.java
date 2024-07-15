package com.example.demo.controller;

import com.example.demo.dto.member.MemberSaveDto;
import com.example.demo.dto.member.MemberShowDto;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Tag(name = "유저 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "유저 생성")
    @ApiResponses(
        value = {
                @ApiResponse( responseCode = "201", description = "유저 생성 완료"),
                @ApiResponse( responseCode = "400", description = "잘못된 값 입력")
        }
    )
    public ResponseEntity<String> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                       @Valid @RequestBody MemberSaveDto memberSaveDto) {
        memberService.addUser(memberSaveDto);
        return new ResponseEntity<>("생성 완료", HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "유저 목록")
    @ApiResponses(
            value = {
                    @ApiResponse( responseCode = "200", description = "유저 목록 호출 성공")
            }
    )
    public ResponseEntity<List<MemberShowDto>> findAll() {
        List<MemberShowDto> memberSaveDtoList = memberService.findAll();
        return new ResponseEntity<>(memberSaveDtoList, HttpStatus.OK);
    }
}
