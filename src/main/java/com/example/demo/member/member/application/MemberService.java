package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;

public interface MemberService {
    void save(MemberRequest.SaveMember takenMemberRequest);
    String createNewPassword(String takenUsername, String takenEmail);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicated(MemberRequest.DataConfirmMember takenDto);
    void patchNicknameById(Long takenId, String takenNickname);
    void patchPasswordByUsername(Long takenId, String takenPassword);
}
