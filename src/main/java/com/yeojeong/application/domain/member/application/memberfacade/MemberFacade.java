package com.yeojeong.application.domain.member.application.memberfacade;

import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberFacade {
    MemberResponse.FindById findById(Long id);
    void save(MemberRequest.SaveMember dto);
    void delete(Long id);

    MemberResponse.FindById patch(Long id, MemberRequest.Put dto);
    MemberResponse.FindById patchPassword(Long id, MemberRequest.PatchPassword dto);
    void checkPassword(Long id, String password);

    Page<MemberResponse.MemberBoardInfo> findBoardById(Long id, int page);
    Page<MemberResponse.MemberCommentInfo> findCommentById(Long id, int page);
    Page<MemberResponse.MemberPlannerInfo> findPlannerById(Long id, int page);
    List<MemberResponse.MemberLocationInfo> findLocationByDate(Long memberId, Long start, Long end);

    void findPassword(String username, String email);
    void findUsernameByEmail(String email);

    void checkDuplicatedByUsername(String username);
    void checkDuplicatedByNickname(String nickname);

    void checkDuplicatedByEmail(String email);
    void checkAuthCheck(String email, String authKey);
}
