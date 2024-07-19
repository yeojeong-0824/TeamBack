package com.example.demo.service;

import com.example.demo.config.exception.member.NotFoundMemberException;
import com.example.demo.entity.Board;
import com.example.demo.entity.Like;
import com.example.demo.entity.Member;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

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
