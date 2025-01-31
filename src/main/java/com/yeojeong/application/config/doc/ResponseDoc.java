package com.yeojeong.application.config.doc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "400",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "값이 누락되었음",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 400,
                                                        "error": "...의 값이 누락되었습니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "유효성 검사에 실패함",
                                                value = """
                                                {
                                                      "timestamp": "2025-02-01T04:05:48.207059",
                                                      "status": 400,
                                                      "error": "[..., 공백일 수 없습니다]",
                                                      "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "데이터를 처리할 수 없음",
                                                value = """
                                                {
                                                      "timestamp": "2025-02-01T04:05:48.207059",
                                                      "status": 400,
                                                      "error": "[오류 메시지]",
                                                      "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "미디어 타입이 일치하지 않음",
                                                value = """
                                                {
                                                      "timestamp": "2025-02-01T04:05:48.207059",
                                                      "status": 400,
                                                      "error": "데이터 형식이 잘못되었습니다.",
                                                      "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "Json 형식이 잘못 됨",
                                                value = """
                                                {
                                                      "timestamp": "2025-02-01T04:05:48.207059",
                                                      "status": 400,
                                                      "error": "[오류 메시지]",
                                                      "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "403",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "인증되지 않음",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 403,
                                                        "error": "인증되지 않은 사용자 입니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "권한 없음",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 403,
                                                        "error": "...를 작성한 사용자가 아닙니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "올바르지 않은 비밀번호",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 403,
                                                        "error": "비밀번호가 일치하지 않습니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "토큰이 존재하지 않음",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 403,
                                                        "error": "... 토큰이 존재하지 않습니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "데이터를 찾을 수 없음",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 404,
                                                        "error": "...를 찾을 수 없습니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "데이터가 중복 됨",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 409,
                                                        "error": "중복된 ...입니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "413",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "파일의 크기가 너무 큼",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 413,
                                                        "error": "파일 크기가 너무 큽니다.",
                                                        "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "서버 오류",
                                                value = """
                                                {
                                                        "timestamp": "2025-02-01T04:02:51.412976300",
                                                        "status": 500,
                                                        "error": "[오류 메시지]",
                                                        "path": "/..."
                                                }
                                            """
                                        )
                                }
                        )
                )
        }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseDoc {
}
