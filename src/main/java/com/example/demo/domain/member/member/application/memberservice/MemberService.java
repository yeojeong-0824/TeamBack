package com.example.demo.domain.member.member.application.memberservice;

import com.example.demo.domain.member.member.presentation.dto.MemberResponse;
import com.example.demo.domain.member.member.presentation.dto.MemberRequest;
import org.springframework.data.domain.Page;

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
    Page<MemberResponse.CommentInfo> findCommentById(Long takenMemberId, int takenPage);
}
