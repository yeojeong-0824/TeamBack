package com.yeojeong.application.domain.member.application.memberservice;

import com.yeojeong.application.domain.member.domain.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByUsername(String username);
    Member findByEmail(String email);

    void save(Member member);
    void delete(Member member);

    void checkDuplicatedByEmail(String takenEmail);
    void checkDuplicatedByUsername(String takenUsername);
    void checkDuplicatedByNickname(String takenNickname);

    void update(Member entity, Member updateEntity);
    void updatePassword(Member entity, String password);
}
