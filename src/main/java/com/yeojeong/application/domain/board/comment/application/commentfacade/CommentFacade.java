package com.yeojeong.application.domain.board.comment.application.commentfacade;

import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentFacade {
    CommentResponse.FindById save(CommentRequest.Save dto, Long boardId, Long memberId);
    CommentResponse.Delete delete(Long id, Long memberId);
    Page<CommentResponse.FindByBoardId> findByBoardId(Long boardId, int page);
    CommentResponse.FindById update(Long id, Long memberId, CommentRequest.Put dto);
}
