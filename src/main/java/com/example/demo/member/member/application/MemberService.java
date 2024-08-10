package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.presentation.dto.MemberResponse;

public interface MemberService {
    void save(MemberRequest.SaveMember takenMemberRequest);
    MemberResponse.FindMember findById(Long id);
    String createNewPassword(String takenUsername, String takenEmail);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicated(MemberRequest.DataConfirmMember takenDto);
    void patchNicknameById(Long takenId, String takenNickname);
    void patchPasswordByUsername(Long takenId, String takenPassword);
}
