package com.yeojeong.application.domain.board.comment.application.commentservice;

import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    Comment findById(Long id);
    Comment save(Comment entity);
    Page<Comment> findByBoardId(Long boardId, int page);
    List<Comment> findByMemberId(Long memberId);
    Comment update(Comment entity);
    void delete(Comment entity);

    void deleteByMemberId(Long memberId);
    void deleteByBoardId(Long boardId);
}
