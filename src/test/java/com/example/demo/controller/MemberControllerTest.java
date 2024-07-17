package com.example.demo.controller;

import com.example.demo.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @MockBean MemberService memberService;

    @Nested
    @DisplayName("아이디 중복검사 테스트")
    class UsernameTest {
        @Test
        @WithMockUser // @WithAnonymousUser로 하면 401 상태코드가 나와서 User 권한으로 테스트
        @DisplayName("아이디 중복검사 테스트")
        void 아이디_중복검사1() throws Exception {

            String username = "user12";

            given(memberService.checkDuplicatedUsername(username)).willReturn(true);
            mvc.perform(get("/member/username/"+username))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("중복된 아이디 테스트")
        void 아이디_중복검사2() throws Exception {

            String username = "user12";

            given(memberService.checkDuplicatedUsername(username)).willReturn(false);
            mvc.perform(get("/member/username/"+username))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 아이디 테스트")
        void 아이디_중복검사_BAD1() throws Exception {

            String[] usernameArr = new String[]{"user", "1234567890123456789012345678901"};

            // 5 이상이여야 함
            for(String username : usernameArr) {
                given(memberService.checkDuplicatedUsername(username)).willReturn(true);
                mvc.perform(get("/member/username/"+username))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("닉네임 중복검사 테스트")
    class NicknameTest {
        @Test
        @WithMockUser
        @DisplayName("닉네임 중복검사 테스트")
        void 닉네임_중복검사1() throws Exception {

            String nickname = "nickname";

            given(memberService.checkDuplicatedNickname(nickname)).willReturn(true);
            mvc.perform(get("/member/nickname/"+nickname))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("중복된 닉네임 테스트")
        void 닉네임_중복검사2() throws Exception {

            String nickname = "nickname";

            given(memberService.checkDuplicatedNickname(nickname)).willReturn(false);
            mvc.perform(get("/member/nickname/"+nickname))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 닉네임 테스트")
        void 닉네임_중복검사_BAD1() throws Exception {

            String[] nicknameArr = new String[]{"12345678901"};

            for(String nickname : nicknameArr) {
                given(memberService.checkDuplicatedNickname(nickname)).willReturn(false);
                mvc.perform(get("/member/nickname/"+nickname))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("이메일 중복검사 테스트")
    class EmailTest {
        @Test
        @WithMockUser
        @DisplayName("이메일 중복검사 테스트")
        void 이메일_중복검사1() throws Exception {

            String email = "test@naver.com";

            given(memberService.checkDuplicatedEmail(email)).willReturn(true);
            mvc.perform(get("/member/email/"+email))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 이메일 테스트")
        void 이메일_중복검사2() throws Exception {

            String[] emailArr = new String[]{"test", "test@naver", "test@naver", "test@naver.", "test@naver.c",
                    "testnaver.com", "@naver.com", "test.naver@com"};

            for(String email : emailArr) {
                given(memberService.checkDuplicatedEmail(email)).willReturn(false);
                mvc.perform(get("/member/email/"+email))
                        .andExpect(status().isBadRequest());
            }
        }

        @Test
        @WithMockUser // @WithAnonymousUser로 하면 401 상태코드가 나와서 User 권한으로 테스트
        @DisplayName("긴 이메일 중복검사 테스트")
        void 이메일_중복검사_BAD1() throws Exception {

            String email = "123456789012345678901234567890123456789012345678901";

            given(memberService.checkDuplicatedEmail(email)).willReturn(false);
            mvc.perform(get("/member/email/"+email))
                    .andExpect(status().isBadRequest());
        }
    }
}