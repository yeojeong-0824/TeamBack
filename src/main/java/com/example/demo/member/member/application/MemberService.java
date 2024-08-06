package com.example.demo.member.member.application;

import com.example.demo.config.GlobalService;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService extends GlobalService<MemberRequest.DefaultMember, Long> {
    public String findEmailByUsername(String takenUsername);

    public String findUsernameByEmail(String takenEmail);

    public boolean checkDuplicatedByUsername(String takenUsername);

    public boolean checkDuplicatedByNickname(String takenNickname);

    public boolean checkDuplicatedByEmail(String takenEmail);

    public void patchPasswordByUsername(String takenUsername, String takenPassword);

    public void patchNicknameByUsername(String takenUsername, String takenNickname);
}
