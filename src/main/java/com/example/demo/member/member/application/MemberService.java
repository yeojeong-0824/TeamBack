package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addUser(MemberRequest.CreateMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.CreateMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }

    public String findEmailByUsername(String username) {
        Member savedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getEmail();
    }

    public String findUsernameByEmail(String email) {
        Member savedMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getUsername();
    }

    public boolean checkDuplicatedByUsername(String takenUsername) {
        return !memberRepository.existsByUsername(takenUsername);
    }

    public boolean checkDuplicatedByNickname(String takenNickname) {
        return !memberRepository.existsByNickname(takenNickname);
    }

    public boolean checkDuplicatedByEmail(String takenEmail) {
        return !memberRepository.existsByEmail(takenEmail);
    }

    @Transactional
    public void patchPasswordByUsername(String username, String password) {
        Member savedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        savedMember.patchPassword(passwordEncoder.encode(password));
        memberRepository.save(savedMember);
    }

    @Transactional
    public void patchNicknameByUsername(String username, String nickname) {
        Member savedMember = memberRepository.findByUsername(nickname)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        savedMember.patchNickname(nickname);
        memberRepository.save(savedMember);
    }
}
