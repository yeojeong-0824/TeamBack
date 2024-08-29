package com.example.demo.board.comment.domain;

import com.example.demo.board.board.domain.Board;
import com.example.demo.member.member.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoardId(Long boardId, Pageable pageable);
    Page<Comment> findByMember(Member member, Pageable pageable);
}
