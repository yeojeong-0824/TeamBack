package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.dto.member.MemberRequest;
import com.example.demo.dto.member.MemberResponse;
import com.example.demo.dto.member.MemberRole;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addUser(MemberRequest takenMemberRequest) {
        Member takenMember = toEntity(takenMemberRequest);
        memberRepository.save(takenMember);
    }

    public boolean checkDuplicatedUsername(String username) {
        return !memberRepository.existsByUsername(username);
    }

    public boolean checkDuplicatedNickname(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public List<MemberResponse> findAll() {
        List<Member> savedMemberList = memberRepository.findAll();
        return savedMemberList.stream().map(this::toDto).toList();
    }

    private Member toEntity(MemberRequest memberRequest) {
        return Member.builder()
                .username(memberRequest.getUsername())
                .nickname(memberRequest.getNickname())
                .phoneNumber(memberRequest.getPhoneNumber())
                .name(memberRequest.getName())
                .age(memberRequest.getAge())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .role(MemberRole.USER.getRole())
                .build();
    }

    private MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .name(member.getName())
                .age(member.getAge())
                .build();
    }
}
