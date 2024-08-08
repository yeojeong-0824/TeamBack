package com.example.demo.board.like.application;

import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.board.board.domain.Board;
import com.example.demo.board.like.domain.Like;
import com.example.demo.member.member.domain.Member;
import com.example.demo.board.like.domain.LikeRepository;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeServiceImpl {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;

    // 좋아요 누른 게시글 리스트
    public List<Board> getLikedBoardsByMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("해당 회원을 찾을 수 없습니다."));

        List<Like> list = likeRepository.findByMember(member);

        return list.stream()
                .map(Like::getBoard)
                .collect(Collectors.toList());
    }


}
