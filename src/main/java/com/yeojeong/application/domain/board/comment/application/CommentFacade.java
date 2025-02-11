package com.yeojeong.application.domain.board.comment.application;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.domain.board.board.application.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentFacade {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @Transactional
    public void save(CommentRequest.Save dto, Long boardId, Long memberId) {
        Board board = boardService.findById(boardId);
        Member member = memberService.findById(memberId);

        Comment entity = CommentRequest.Save.toEntity(dto, board, member);
        commentService.save(entity);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Comment savedEntity = commentService.findByIdAuth(id, memberId);
        commentService.delete(savedEntity);
    }

    public Page<CommentResponse.FindByBoardId> findByBoardId(Long boardId, int page) {
        Page<Comment> entityPage = commentService.findByBoardId(boardId, page);
        return entityPage.map(CommentResponse.FindByBoardId::toDto);
    }

    @Transactional
    public void update(Long id, Long memberId, CommentRequest.Put dto) {
        Comment savedEntity = commentService.findByIdAuth(id, memberId);
        Comment updateEntity = CommentRequest.Put.toEntity(dto);
        commentService.update(savedEntity, updateEntity);
    }
}
