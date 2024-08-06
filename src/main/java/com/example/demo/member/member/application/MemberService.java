package com.example.demo.member.member.application;

import com.example.demo.config.GlobalService;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService extends GlobalService<MemberRequest.DefaultMember, Long> {
    String findEmailByUsername(String takenUsername);
    String findUsernameByEmail(String takenEmail);
    boolean checkDuplicatedByUsername(String takenUsername);
    boolean checkDuplicatedByNickname(String takenNickname);
    boolean checkDuplicatedByEmail(String takenEmail);
    void patchPasswordByUsername(String takenUsername, String takenPassword);
    void patchNicknameByUsername(String takenUsername, String takenNickname);
}
