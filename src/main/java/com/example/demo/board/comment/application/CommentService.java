package com.example.demo.board.comment.application;

import com.example.demo.board.comment.presentation.dto.CommentRequest;

public interface CommentService {
    void save(CommentRequest.Save takenDto, Long takenBoardId, Long takenMemberId);
}
