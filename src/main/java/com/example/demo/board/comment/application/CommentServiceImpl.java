package com.example.demo.board.comment.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.domain.CommentRepository;
import com.example.demo.board.comment.exception.NotFoundCommentException;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentSaveRequest;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentUpdateRequest;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.util.SecurityUtil;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    @Override
    public void save(Long boardId, CommentSaveRequest request) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundDataException("해당 회원을 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .body(request.body())
                .build();

        commentRepository.save(comment);

        // 게시글 댓글 수 증가
        board.commentCountUp();
    }

    // 댓글 수정
    @Transactional
    @Override
    public void updateById(Long commentId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException("해당 댓글을 찾을 수 없습니다."));

        comment.update(request);
    }

    // 댓글 삭제
    @Transactional
    @Override
    public void deleteById(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다."));

        Board board = boardRepository.findById(comment.getBoard().getId())
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        // 게시글 댓글 수 감소
        board.commentCountDown();
        commentRepository.delete(comment);
    }
}
