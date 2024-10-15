package com.yeojeong.application.domain.board.comment.application.commentservice;

import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment findById(Long id);
    Comment save(Comment entity);
    Page<Comment> findByBoardId(Long boardId, int page);
    Page<Comment> findByMemberId(Long memberId, int page);
    Comment updateById(Comment entity, Long memberId, Comment updateEntity);
    void delete(Comment entity, Long memberId);
}
