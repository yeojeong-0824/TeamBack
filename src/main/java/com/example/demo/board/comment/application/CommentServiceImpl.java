package com.example.demo.board.comment.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.domain.CommentRepository;
import com.example.demo.board.comment.exception.NotFoundCommentException;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentSaveRequest;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentUpdateRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.exception.RequestDataException;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public void save(Long memberId, Long boardId, CommentSaveRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundDataException("해당 회원을 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .body(request.body())
                .member(member)
                .board(board)
                .build();

        commentRepository.save(comment);

        // 게시글 댓글 수 증가
        board.commentCountUp();
    }

    @Transactional
    @Override
    public List<CommentResponse.CommentListResponse> findByBoardId(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId);
        return toDtoList(commentList);
    }

    // 댓글 수정
    @Transactional
    @Override
    public void updateById(Long memberId, Long commentId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException("해당 댓글을 찾을 수 없습니다."));

        boardRepository.findById(comment.getBoard().getId())
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        if (!memberId.equals(comment.getMember().getId())) {
            throw new RequestDataException("댓글을 작성한 회원이 아닙니다.");
        }

        comment.update(request);
    }

    // 댓글 삭제
    @Transactional
    @Override
    public void deleteById(Long memberId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다."));

        Board board = boardRepository.findById(comment.getBoard().getId())
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        if (!memberId.equals(comment.getMember().getId())) {
            throw new RequestDataException("댓글을 작성한 회원이 아닙니다.");
        }

        // 게시글 댓글 수 감소
        board.commentCountDown();
        commentRepository.delete(comment);
    }

    public List<CommentResponse.CommentListResponse> toDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(comment -> new CommentResponse.CommentListResponse(comment.getId(), comment.getBody(), comment.getMember().getNickname()))
                .collect(Collectors.toList());
    }
}
