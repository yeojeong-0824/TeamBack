package com.example.demo.controller;

import com.example.demo.dto.member.MemberRequest.*;
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

import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("회원가입 테스트")
    class AddMemberTest {
        @Test
        @WithMockUser
        @DisplayName("회원가입 테스트")
        void 회원가입1() throws Exception {

            CreateMember data = CreateMember.builder()
                    .username("user12").nickname("소인국 갔다옴").email("user@naver.com")
                    .password("1q2w3e4r").name("걸리버").age(90).build();

            String json = objectMapper.writeValueAsString(data);

            mvc.perform(post("/member")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 값을 넣은 회원가입 테스트")
        void 회원가입_중복된_값() throws Exception {

            List<CreateMember> dataList = new ArrayList<>();

            //잘못된 아이디
            dataList.add(CreateMember.builder().username("user").nickname("test").email("user@naver.com")
                    .password("1q2w3e4r").name("걸리버").age(90).build());
            dataList.add(CreateMember.builder().username("1234567890123456789012345678901").nickname("test")
                    .email("user@naver.com").password("1q2w3e4r").name("걸리버").age(90).build());

            //잘못된 이메일
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("usernaver.com")
                    .password("1q2w3e4r").name("걸리버").age(90).build());
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@navercom")
                    .password("1q2w3e4r").name("걸리버").age(90).build());

            //잘못된 비밀번호
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("a").name("걸리버").age(90).build());
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("12345678").name("걸리버").age(90).build());
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("aaaaaaaa").name("걸리버").age(90).build());

            //잘못된 이름
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("1q2w3e4r").name("12345678901").age(90).build());

            //잘못된 나이
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("1q2w3e4r").name("걸리버").age(-1).build());
            dataList.add(CreateMember.builder().username("user12").nickname("test").email("user@naver.com")
                    .password("1q2w3e4r").name("걸리버").age(130).build());

            for(CreateMember data : dataList) {
                String json = objectMapper.writeValueAsString(data);

                mvc.perform(post("/member")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("중복검사 테스트")
    class confirm {
        @Nested
        @DisplayName("아이디 중복검사 테스트")
        class UsernameTest {
            @Test
            @WithMockUser // @WithAnonymousUser로 하면 401 상태코드가 나와서 User 권한으로 테스트
            @DisplayName("중복 검사 테스트")
            void 중복검사1() throws Exception {

                String data = "user12";

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                    DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                    DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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

                    DataConfirmMember memberDuplicated = DataConfirmMember.builder()
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
}