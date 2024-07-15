package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.dto.member.MemberSaveDto;
import com.example.demo.dto.member.MemberShowDto;
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
    public void addUser(MemberSaveDto takenMemberSaveDto) {
        Member takenMember = toEntity(takenMemberSaveDto);
        memberRepository.save(takenMember);
    }

    public boolean checkDuplicatedUsername(String username) {
        return !memberRepository.existsByUsername(username);
    }

    public boolean checkDuplicatedNickname(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public List<MemberShowDto> findAll() {
        List<Member> savedMemberList = memberRepository.findAll();
        return savedMemberList.stream().map(this::toDto).toList();
    }

    private Member toEntity(MemberSaveDto memberSaveDto) {
        return Member.builder()
                .username(memberSaveDto.getUsername())
                .nickname(memberSaveDto.getNickname())
                .phoneNumber(memberSaveDto.getPhoneNumber())
                .name(memberSaveDto.getName())
                .age(memberSaveDto.getAge())
                .password(passwordEncoder.encode(memberSaveDto.getPassword()))
                .role(MemberRole.USER.getRole())
                .build();
    }

    private MemberShowDto toDto(Member member) {
        return MemberShowDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .name(member.getName())
                .age(member.getAge())
                .build();
    }
}
