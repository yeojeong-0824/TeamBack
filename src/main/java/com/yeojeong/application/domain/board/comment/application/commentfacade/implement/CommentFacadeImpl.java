package com.yeojeong.application.domain.board.comment.application.commentfacade.implement;

import com.yeojeong.application.config.exception.RestApiException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.application.commentfacade.CommentFacade;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @Override
    public CommentResponse.FindById save(CommentRequest.Save dto, Long boardId, Long memberId) {
        Board board = boardService.findById(boardId);
        Member member = memberService.findById(memberId);

        Comment entity = CommentRequest.Save.toEntity(dto, board, member);
        Comment savedEntity = commentService.save(entity);

        if(dto.score() != 0) boardService.createComment(board);
        return CommentResponse.FindById.toDto(savedEntity);
    }

    @Override
    public CommentResponse.Delete delete(Long id, Long memberId) {
        Comment savedEntity = commentService.findById(id);
        checkMember(savedEntity, memberId);

        Board board = savedEntity.getBoard();
        commentService.delete(savedEntity);

        if(savedEntity.getScore() != 0) boardService.deleteComment(board);
        return CommentResponse.Delete.toDto(board);
    }

    @Override
    public Page<CommentResponse.FindByBoardId> findByBoardId(Long boardId, int page) {
        Page<Comment> entityPage = commentService.findByBoardId(boardId, page);
        return entityPage.map(CommentResponse.FindByBoardId::toDto);
    }

    @Override
    public CommentResponse.FindById update(Long id, Long memberId, CommentRequest.Put dto) {
        Comment savedEntity = commentService.findById(id);
        checkMember(savedEntity, memberId);

        Comment entity = CommentRequest.Put.toEntity(dto);
        Comment rtnEntity = commentService.update(savedEntity, entity);

        Board board = rtnEntity.getBoard();
        boardService.updateComment(board);

        return CommentResponse.FindById.toDto(rtnEntity);
    }

    private void checkMember(Comment comment, Long memberId) {
        if(memberId.equals(comment.getMember().getId())) throw new RestApiException(ErrorCode.USER_MISMATCH);
    }
}
