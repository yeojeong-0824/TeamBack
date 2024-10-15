package com.yeojeong.application.domain.member.member.application.memberservice.implement;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.domain.CommentRepository;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.config.exception.DuplicatedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
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
            throw new DuplicatedException("중복된 아이디입니다");
    }

    @Override
    public void checkDuplicatedByNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname))
            throw new DuplicatedException("중복된 닉네임입니다");
    }

    @Override
    public void checkDuplicatedByEmail(String email) {
        if(memberRepository.existsByEmail(email))
            throw new DuplicatedException("중복된 이메일입니다");
    }

    @Transactional
    @Override
    public Member patch(Member member, Member updateMember) {
        member.patchMember(updateMember);
        return memberRepository.save(member);
    }
}
