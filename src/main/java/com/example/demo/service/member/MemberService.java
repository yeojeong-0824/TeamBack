package com.example.demo.service.member;

import com.example.demo.config.exception.member.NotFoundMemberException;
import com.example.demo.dto.member.MemberRequest.*;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.dto.member.MemberResponse.*;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
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
    public void addUser(CreateMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = CreateMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
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

    public String findMemberEmailByUsername(String username) {
        Member savedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getEmail();
    }

    public String findEmailByUsername(String username) {
        Member savedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getEmail();
    }

    public void patchPasswordByUsername(String username, String password) {
        Member savedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        savedMember.patchPassword(passwordEncoder.encode(password));
        memberRepository.save(savedMember);
    }
}
