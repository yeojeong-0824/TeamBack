package com.yeojeong.application.domain.member.member.application.memberservice;

import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import org.springframework.data.domain.Page;

public interface MemberService {
    Member findById(Long id);
    void save(MemberRequest.SaveMember takenMemberRequest);
    void deleteByMemberId(Long takenMemberId, MemberRequest.DeleteMember takenDto);
    String findPassword(String takenUsername, String takenEmail);
    String findUsernameByEmail(String takenEmail);
    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicatedByUsername(String takenUsername);
    void checkDuplicatedByNickname(String takenNickname);
    MemberResponse.FindMember patchById(Long takenMemberId, MemberRequest.PatchMember takenDto);
    MemberResponse.FindMember patchPasswordById(Long takenMemberId, MemberRequest.PatchPassword takenDto);
    Page<MemberResponse.BoardInfo> findBoardById(Long takenMemberId, int takenPage);
    Page<MemberResponse.CommentInfo> findCommentById(Long takenMemberId, int takenPage);
}
