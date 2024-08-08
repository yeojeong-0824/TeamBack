package com.example.demo.board.comment.application;

import com.example.demo.board.comment.presentation.dto.CommentRequest;

public interface CommentService {

    void save(Long boardId, CommentRequest.CommentSaveRequest request);
    void updateById(Long commentId, CommentRequest.CommentUpdateRequest request);
    void deleteById(Long commentId);

}
