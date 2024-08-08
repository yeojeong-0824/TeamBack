package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;

public interface MemberService {
    void save(MemberRequest.DefaultMember takenMemberRequest);
    String findEmailByUsername(String takenUsername);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void patchPasswordByUsername(String takenUsername, String takenPassword);
    void patchNicknameByUsername(String takenUsername, String takenNickname);
    void checkDuplicated(MemberRequest.DataConfirmMember takenDto);
}
