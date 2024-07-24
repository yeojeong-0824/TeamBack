package com.example.demo.service.member;

import com.example.demo.dto.member.MemberRequest.*;
import com.example.demo.entity.Member;
import com.example.demo.dto.member.MemberResponse;
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

    public boolean checkDuplicatedUsername(String takenUsername) {
        return !memberRepository.existsByUsername(takenUsername);
    }

    public boolean checkDuplicatedNickname(String takenNickname) {
        return !memberRepository.existsByNickname(takenNickname);
    }

    public boolean checkDuplicatedEmail(String takenEmail) {
        return !memberRepository.existsByEmail(takenEmail);
    }

    private MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .name(member.getName())
                .age(member.getAge())
                .build();
    }
}
