package com.example.demo.board.comment.application;

import com.example.demo.board.board.exception.NotFoundBoardException;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.domain.CommentRepository;
import com.example.demo.board.comment.exception.NotFoundCommentException;
import com.example.demo.board.comment.presentation.dto.CommentRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import com.example.demo.member.member.exception.NotFoundMemberException;
import com.example.demo.board.board.domain.Board;
import com.example.demo.member.member.domain.Member;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    public CommentResponse.CommentSaveResponse writeComment(CommentRequest.CommentSaveRequest request, Long boardId, String memberName) {

        Member member = memberRepository.findByUsername(memberName)
                .orElseThrow(() -> new NotFoundMemberException("해당 회원을 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = toEntity(request, member, board);
        commentRepository.save(comment);

        // 게시글 댓글 수 증가
        board.commentCountUp();

        return new CommentResponse.CommentSaveResponse(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse.CommentSaveResponse updateComment(CommentRequest.CommentSaveRequest request, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException("해당 댓글을 찾을 수 없습니다."));

        comment.update(request);
        return new CommentResponse.CommentSaveResponse(comment);
    }

    // 댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException("해당 댓글을 찾을 수 없습니다."));
        Board board = boardRepository.findById(comment.getBoard().getId())

                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));

        // 게시글 댓글 수 담소
        board.commentCountDown();
        commentRepository.delete(comment);

        return commentId;
    }

    private Comment toEntity(CommentRequest.CommentSaveRequest request, Member member, Board board) {
        return Comment.builder()
                .body(request.body())
                .member(member)
                .board(board)
                .build();
    }

}
