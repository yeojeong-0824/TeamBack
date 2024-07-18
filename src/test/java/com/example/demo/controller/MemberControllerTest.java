package com.example.demo.controller;

import com.example.demo.dto.member.MemberDuplicated;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @MockBean MemberService memberService;
    @Autowired ObjectMapper objectMapper;

    @Nested
    @DisplayName("아이디 중복검사 테스트")
    class UsernameTest {
        @Test
        @WithMockUser // @WithAnonymousUser로 하면 401 상태코드가 나와서 User 권한으로 테스트
        @DisplayName("중복 검사 테스트")
        void 중복검사1() throws Exception {

            String data = "user12";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .username(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedUsername(data)).willReturn(true);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("중복 테스트")
        void 중복검사2() throws Exception {

            String data = "user12";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .username(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedUsername(data)).willReturn(false);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 값 테스트")
        void 중복검사_BAD1() throws Exception {

            String[] dataArr = new String[]{"user", "1234567890123456789012345678901"};

            for(String data : dataArr) {

                MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                        .username(data)
                        .build();

                String json = objectMapper.writeValueAsString(memberDuplicated);

                given(memberService.checkDuplicatedUsername(data)).willReturn(false);
                mvc.perform(post("/member/confirm")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("닉네임 중복검사 테스트")
    class NicknameTest {
        @Test
        @WithMockUser
        @DisplayName("중복 검사 테스트")
        void 중복검사1() throws Exception {

            String data = "nickname";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .nickname(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedNickname(data)).willReturn(true);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("중복 테스트")
        void 중복검사2() throws Exception {

            String data = "nickname";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .nickname(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedNickname(data)).willReturn(false);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 값 테스트")
        void 중복검사_BAD1() throws Exception {

            String[] dataArr = new String[]{"12345678901"};

            for(String data : dataArr) {

                MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                        .nickname(data)
                        .build();

                String json = objectMapper.writeValueAsString(memberDuplicated);

                given(memberService.checkDuplicatedNickname(data)).willReturn(false);
                mvc.perform(post("/member/confirm")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("이메일 중복검사 테스트")
    class EmailTest {
        @Test
        @WithMockUser
        @DisplayName("중복 검사 테스트")
        void 중복검사1() throws Exception {

            String data = "test@naver.com";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .email(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedEmail(data)).willReturn(true);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("중복 테스트")
        void 중복검사2() throws Exception {

            String data = "test@naver.com";

            MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                    .email(data)
                    .build();

            String json = objectMapper.writeValueAsString(memberDuplicated);

            given(memberService.checkDuplicatedEmail(data)).willReturn(false);
            mvc.perform(post("/member/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 값 테스트")
        void 중복검사_BAD1() throws Exception {

            String[] dataArr = new String[]{"test", "test@naver", "test@naver", "test@naver.", "test@naver.c",
                    "testnaver.com", "@naver.com", "test.naver@com", "123456789012345678901234567890123456789012345678901"};

            for(String data : dataArr) {

                MemberDuplicated memberDuplicated = MemberDuplicated.builder()
                        .email(data)
                        .build();

                String json = objectMapper.writeValueAsString(memberDuplicated);

                given(memberService.checkDuplicatedNickname(data)).willReturn(false);
                mvc.perform(post("/member/confirm")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}