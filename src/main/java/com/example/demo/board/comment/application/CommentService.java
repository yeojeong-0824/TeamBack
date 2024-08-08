package com.example.demo.board.comment.application;

import com.example.demo.board.comment.presentation.dto.CommentRequest.*;

public interface CommentService {

    void save(Long boardId, CommentSaveRequest request);

    void update(Long commentId, CommentUpdateRequest request);

    void delete(Long commentId);
}
