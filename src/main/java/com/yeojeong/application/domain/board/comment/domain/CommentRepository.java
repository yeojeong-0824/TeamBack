package com.yeojeong.application.domain.board.comment.domain;

import com.yeojeong.application.domain.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoardId(Long boardId, Pageable pageable);
    Page<Comment> findAllMemberId(Long memberId, Pageable pageable);
    void deleteByMember(Member member);
}
