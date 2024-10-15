package com.yeojeong.application.domain.member.member.application.memberservice.implement;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.config.exception.DuplicatedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    @Override
    public void save(Member entity) {
        memberRepository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Member entity) {
        memberRepository.delete(entity);
    }

    @Transactional
    @Override
    public void pathPassword(Member entity, String password) {
        memberRepository.save(entity);
    }


    @Override
    public void checkDuplicatedByUsername(String username) {
        if(memberRepository.existsByUsername(username))
            throw new DuplicatedException(ErrorCode.DUPLICATED_ID);
    }

    @Override
    public void checkDuplicatedByNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname))
            throw new DuplicatedException(ErrorCode.DUPLICATED_NICKNAME);
    }

    @Override
    public void checkDuplicatedByEmail(String email) {
        if(memberRepository.existsByEmail(email))
            throw new DuplicatedException(ErrorCode.DUPLICATED_EMAIL);
    }

    @Transactional
    @Override
    public Member patch(Member member, Member updateMember) {
        member.patchMember(updateMember);
        return memberRepository.save(member);
    }
}
