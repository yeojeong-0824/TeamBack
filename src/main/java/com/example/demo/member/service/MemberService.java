package com.example.demo.member.service;

import com.example.demo.member.domain.Member;
import com.example.demo.member.presentation.MemberSaveDto;
import com.example.demo.member.presentation.MemberShowDto;
import com.example.demo.member.repository.MemberRepository;
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
