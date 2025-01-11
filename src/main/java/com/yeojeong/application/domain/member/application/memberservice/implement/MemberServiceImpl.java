package com.yeojeong.application.domain.member.application.memberservice.implement;

import com.yeojeong.application.config.exception.DuplicatedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다."));
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다."));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다."));
    }

    @Override
    public void save(Member entity) {
        memberRepository.save(entity);
    }

    @Override
    public void delete(Member entity) {
        memberRepository.delete(entity);
    }


    @Override
    public void checkDuplicatedByUsername(String username) {
        if(memberRepository.existsByUsername(username))
            throw new DuplicatedException("중복된 아이디 입니다.");
    }

    @Override
    public void checkDuplicatedByNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname))
            throw new DuplicatedException("중복된 닉네임 입니다");
    }

    @Override
    public void checkDuplicatedByEmail(String email) {
        if(memberRepository.existsByEmail(email))
            throw new DuplicatedException("중복된 이메일 입니다.");
    }

    @Override
    public void update(Member entity, Member updateEntity) {
        entity.updateMember(updateEntity);
        memberRepository.save(entity);
    }

    public void updatePassword(Member entity, String password) {
        entity.updatePassword(password);
        memberRepository.save(entity);
    }
}
