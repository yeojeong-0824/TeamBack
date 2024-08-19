package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.presentation.dto.MemberResponse;

public interface MemberService {
    void save(MemberRequest.SaveMember takenMemberRequest);
    void deleteByMemberId(Long takenMemberId);
    MemberResponse.FindMember findById(Long takenMemberId);
    String createNewPassword(String takenUsername, String takenEmail);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicated(MemberRequest.DataConfirmMember takenDto);
    void patchNicknameById(Long takenMemberId, String takenNickname);
    void patchPasswordByUsername(Long takenMemberId, String takenPassword);

    MemberResponse.FindMemberDetail findByIdDetail(Long takenMemberId);
}
