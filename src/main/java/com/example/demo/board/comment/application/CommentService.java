package com.example.demo.board.comment.application;

import com.example.demo.board.comment.presentation.dto.CommentRequest;

public interface CommentService {

    void save(Long memberId, Long boardId, CommentRequest.CommentSaveRequest request);
    void updateById(Long memberId, Long commentId, CommentRequest.CommentUpdateRequest request);
    void deleteById(Long memberId, Long commentId);

}
