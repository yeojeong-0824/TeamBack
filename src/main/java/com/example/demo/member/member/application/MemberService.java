package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;

public interface MemberService {
    void save(MemberRequest.DefaultMember takenMemberRequest);
    String findEmailByUsername(String takenUsername);
    String findUsernameByEmail(String takenEmail);
    boolean checkDuplicatedByUsername(String takenUsername);
    boolean checkDuplicatedByNickname(String takenNickname);
    boolean checkDuplicatedByEmail(String takenEmail);
    void patchPasswordByUsername(String takenUsername, String takenPassword);
    void patchNicknameByUsername(String takenUsername, String takenNickname);
}
