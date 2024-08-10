package com.example.demo.member.member.application;

import com.example.demo.config.exception.DuplicatedException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String createNewPassword(String takenUsername, String takenEmail) {
        Member savedMember = memberRepository.findByUsername(takenUsername)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        if(!savedMember.getEmail().equals(takenEmail)) throw new NotFoundDataException("해당 유저의 이메일을 찾지 못했습니다");

        String newPassword = this.createNewPassword();
        this.patchPasswordByUsername(savedMember.getId(), newPassword);

        return newPassword;
    }

    private String createNewPassword() {
        String password = "";
        for(int i = 0; i < 4; i++) {
            password += (char) ((int) (Math.random() * 26) + 97);
        }
        for(int i = 0; i < 4; i++) {
            password += (int) (Math.random() * 10);
        }
        return password;
    }

    @Override
    public String findUsernameByEmail(String takenEmail) {
        Member savedMember = memberRepository.findByEmail(takenEmail)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return savedMember.getUsername();
    }

    @Override
    public void checkDuplicated(MemberRequest.DataConfirmMember takenDataConfirmMember) {
        String takenNickname = takenDataConfirmMember.nickname();
        String takenUsername = takenDataConfirmMember.username();

        if(memberRepository.existsByNickname(takenNickname)) throw new DuplicatedException("중복된 닉네임입니다");
        if(memberRepository.existsByUsername(takenUsername)) throw new DuplicatedException("중복된 아이디입니다");
    }

    @Override
    public void patchNicknameById(Long takenId, String takenNickname) {
        Member savedMember = memberRepository.findById(takenId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        savedMember.patchNickname(takenNickname);
        memberRepository.save(savedMember);
    }

    @Transactional
    @Override
    public void patchPasswordByUsername(Long takenId, String takenPassword) {
        Member savedMember = memberRepository.findById(takenId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        savedMember.patchPassword(passwordEncoder.encode(takenPassword));
        memberRepository.save(savedMember);
    }

    @Override
    public void checkDuplicatedByEmail(String takenEmail) {
        if(memberRepository.existsByEmail(takenEmail)) throw new DuplicatedException("중복된 이메일입니다");
    }

    @Transactional
    @Override
    public void save(MemberRequest.SaveMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.SaveMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }

    @Override
    public MemberResponse.FindMember findById(Long id) {
        Member savedMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
        return MemberResponse.FindMember.toDto(savedMember);
    }
}
