package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberService {
    void save(MemberRequest.SaveMember takenMemberRequest);
    void deleteByMemberId(Long takenMemberId, MemberRequest.DeleteMember takenDto);
    MemberResponse.FindMember findById(Long takenMemberId);
    String createNewPassword(String takenUsername, String takenEmail);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicated(MemberRequest.DataConfirmMember takenDto);
    void patchById(Long takenMemberId, MemberRequest.PatchMember takenDto);
    Page<MemberResponse.BoardInfo> findBoardById(Long takenMemberId, int takenPage);
    Page<MemberResponse.BoardScoreInfo> findBoardScoreById(Long takenMemberId, int takenPage);
}
