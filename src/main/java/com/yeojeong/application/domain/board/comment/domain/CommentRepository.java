package com.yeojeong.application.domain.board.comment.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoardId(Long boardId, Pageable pageable);
    List<Comment> findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
    void deleteByBoardId(Long boardId);
}
