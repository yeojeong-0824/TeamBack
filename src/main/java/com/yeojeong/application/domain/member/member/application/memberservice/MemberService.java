package com.yeojeong.application.domain.member.member.application.memberservice;

import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import org.springframework.data.domain.Page;

public interface MemberService {
    Member findById(Long id);
    Member findByUsername(String username);
    Member findByEmail(String email);

    void save(Member member);
    void delete(Member member);
    void pathPassword(Member member, String password);

    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicatedByUsername(String takenUsername);
    void checkDuplicatedByNickname(String takenNickname);

    Member patch(Member member, Member updateMember);
}
