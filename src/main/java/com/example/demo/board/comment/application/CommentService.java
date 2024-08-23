package com.example.demo.board.comment.application;

import com.example.demo.board.comment.presentation.dto.CommentRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import java.util.List;

public interface CommentService {

    void save(Long memberId, Long boardId, CommentRequest.CommentSaveRequest request);
    List<CommentResponse.CommentListResponse> findByBoardId(Long boardId);
    void updateById(Long memberId, Long commentId, CommentRequest.CommentUpdateRequest request);
    void deleteById(Long memberId, Long commentId);
}
