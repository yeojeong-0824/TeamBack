package com.yeojeong.application.domain.board.comment.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoardId(Long boardId, Pageable pageable);
    Page<Comment> findAllByMemberId(Long memberId, Pageable pageable);

    void deleteByMemberId(Long memberId);
}
