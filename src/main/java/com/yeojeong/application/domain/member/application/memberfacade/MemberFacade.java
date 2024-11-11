package com.yeojeong.application.domain.member.application.memberfacade;

import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import org.springframework.data.domain.Page;

public interface MemberFacade {
    MemberResponse.FindById findById(Long id);
    void save(MemberRequest.SaveMember dto);
    void delete(long id, MemberRequest.Delete dto);
    MemberResponse.FindById patch(Long id, MemberRequest.Patch dto);

    Page<MemberResponse.BoardInfo> findBoardById(long id, int page);
    Page<MemberResponse.CommentInfo> findCommentById(long id, int page);

    void findPassword(String username, String email);
    void findUsernameByEmail(String email);

    void checkDuplicatedByUsername(String username);
    void checkDuplicatedByNickname(String nickname);

    void checkDuplicatedByEmail(String email);
    void checkAuthCheck(String email, String authKey);
}