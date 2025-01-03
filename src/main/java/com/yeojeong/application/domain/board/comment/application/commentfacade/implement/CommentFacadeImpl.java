package com.yeojeong.application.domain.board.comment.application.commentfacade.implement;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.application.commentfacade.CommentFacade;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @Override
//    @Transactional
    public CommentResponse.FindById save(CommentRequest.Save dto, Long boardId, Long memberId) {
        Board board = boardService.findById(boardId);
        Member member = memberService.findById(memberId);

        Comment entity = CommentRequest.Save.toEntity(dto, board, member);
        Comment savedEntity = commentService.save(entity);
        boardService.createComment(board);

        return CommentResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public BoardResponse.FindById delete(Long id, Long memberId) {
        Comment savedEntity = commentService.findById(id);
        checkMember(savedEntity, memberId);

        Board board = savedEntity.getBoard();
        board.commentCountDown();
        boardService.deleteComment(board);

        commentService.delete(savedEntity);
        return BoardResponse.FindById.toDto(board);
    }

    @Override
    public Page<CommentResponse.FindByBoardId> findByBoardId(Long boardId, int page) {
        Page<Comment> entityPage = commentService.findByBoardId(boardId, page);
        return entityPage.map(CommentResponse.FindByBoardId::toDto);
    }

    @Override
    @Transactional
    public CommentResponse.FindById update(Long id, Long memberId, CommentRequest.Put dto) {
        Comment savedEntity = commentService.findById(id);
        checkMember(savedEntity, memberId);

        Comment entity = CommentRequest.Put.toEntity(dto);
        savedEntity.update(entity);
        Comment rtnEntity = commentService.update(savedEntity);

        Board board = rtnEntity.getBoard();
        boardService.updateComment(board);

        return CommentResponse.FindById.toDto(rtnEntity);
    }

    private void checkMember(Comment comment, Long memberId) {
        if(memberId.equals(comment.getMember().getId())) throw new OwnershipException("댓글을 작성한 회원이 아닙니다.");
    }
}
