package com.example.demo.member.member.application;

import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String findEmailByUsername(String takenUsername) {
        Member savedMember = memberRepository.findByUsername(takenUsername)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getEmail();
    }

    @Override
    public String findUsernameByEmail(String takenEmail) {
        Member savedMember = memberRepository.findByEmail(takenEmail)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        return savedMember.getUsername();
    }

    @Override
    public boolean checkDuplicatedByUsername(String takenUsername) {
        return !memberRepository.existsByUsername(takenUsername);
    }

    @Override
    public boolean checkDuplicatedByNickname(String takenNickname) {
        return !memberRepository.existsByNickname(takenNickname);
    }

    @Override
    public boolean checkDuplicatedByEmail(String takenEmail) {
        return !memberRepository.existsByEmail(takenEmail);
    }

    @Transactional
    @Override
    public void patchPasswordByUsername(String takenUsername, String takenPassword) {
        Member savedMember = memberRepository.findByUsername(takenUsername)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        savedMember.patchPassword(passwordEncoder.encode(takenPassword));
        memberRepository.save(savedMember);
    }

    @Transactional
    @Override
    public void patchNicknameByUsername(String takenUsername, String takenNickname) {
        Member savedMember = memberRepository.findByUsername(takenNickname)
                .orElseThrow(() -> new NotFoundMemberException("해당 유저를 찾지 못했습니다"));

        savedMember.patchNickname(takenNickname);
        memberRepository.save(savedMember);
    }

    @Transactional
    @Override
    public void save(MemberRequest.DefaultMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.DefaultMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }

    @Override
    public Page<MemberRequest.DefaultMember> findAll() {
        return null;
    }

    @Override
    public MemberRequest.DefaultMember findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {}

    @Override
    public void updateById(MemberRequest.DefaultMember data, Long id) {}
}
