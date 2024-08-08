package com.example.demo.member.member.application;

import com.example.demo.member.member.exception.DuplicatedException;
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

    @Override
    public void checkDuplicated(MemberRequest.DataConfirmMember takenDataConfirmMember) {
        String takenNickname = takenDataConfirmMember.nickname();
        String takenUsername = takenDataConfirmMember.username();

        if(!memberRepository.existsByNickname(takenNickname)) throw new DuplicatedException("중복된 닉네임입니다");
        if(!memberRepository.existsByUsername(takenUsername)) throw new DuplicatedException("중복된 아이디입니다");
    }

    @Override
    public void checkDuplicatedByEmail(String takenEmail) {
        if(!memberRepository.existsByEmail(takenEmail)) throw new DuplicatedException("중복된 이메일입니다");
    }

    @Transactional
    @Override
    public void save(MemberRequest.DefaultMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.DefaultMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }
}
