package com.example.demo.service;

import com.example.demo.config.exception.board.NotFoundBoardException;
import com.example.demo.dto.comment.CommentRequest.*;
import com.example.demo.dto.comment.CommentResponse.*;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MemberRepository;
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
    public CommentSaveResponse writeComment(CommentSaveRequest request, Long boardId, String memberName){
        Member member = memberRepository.findByUsername(memberName).get();
        Board board = boardRepository.findById(boardId).get();
        Comment comment = toEntity(request, member, board);
        commentRepository.save(comment);

        // 게시글 댓글 수 증가
        board.commentCountUp();

        return new CommentSaveResponse(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentSaveResponse updateComment(CommentSaveRequest request, Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.update(request);
        return new CommentSaveResponse(comment);
    }

    // 댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        Board board = boardRepository.findById(comment.getBoard().getId()).get();

        // 게시글 댓글 수 담소
        board.commentCountDown();

        commentRepository.delete(comment);

        return commentId;
    }

    private Comment toEntity(CommentSaveRequest request, Member member, Board board) {
        return Comment.builder()
                .body(request.body())
                .member(member)
                .board(board)
                .build();
    }

}
