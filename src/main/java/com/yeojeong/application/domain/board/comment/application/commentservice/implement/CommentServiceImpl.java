package com.yeojeong.application.domain.board.comment.application.commentservice.implement;

import com.yeojeong.application.config.exception.RestApiException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.domain.CommentRepository;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.util.customannotation.RedissonLocker;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    @RedissonLocker(key = "saveComment")
    public CommentResponse.FindComment save(CommentRequest.Save takenDto, Long takenBoardId, Long takenMemberId) {
        Board savedBoard = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));

        Member savedMember = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다"));

        Comment entity = CommentRequest.Save.toEntity(takenDto, savedBoard, savedMember);
        Comment save = commentRepository.save(entity);

        savedBoard.commentCountUp();
        int avgScore = getAvgScore(savedBoard);
        savedBoard.avgScorePatch(avgScore);

        boardRepository.save(savedBoard);

        return CommentResponse.FindComment.toDto(save);
    }

    @Override
    @Transactional
    @RedissonLocker(key = "deleteComment")
    public CommentResponse.DeleteComment deleteById(Long takenCommentId, Long takenMemberId){
        Comment savedComment = commentRepository.findById(takenCommentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다"));

        if(!takenMemberId.equals(savedComment.getMember().getId()))
            throw new RestApiException(ErrorCode.USER_MISMATCH);

        commentRepository.delete(savedComment);

        Board savedBoard = savedComment.getBoard();

        savedBoard.commentCountDown();
        int avgScore = getAvgScore(savedBoard);
        savedBoard.avgScorePatch(avgScore);

        Board board = boardRepository.save(savedBoard);
        return CommentResponse.DeleteComment.toDto(board);
    }


    // 게시글 평점 평균 구하는 메서드
    private int getAvgScore(Board board) {
        int commentCount = board.getCommentCount();
        if(commentCount == 0) return 0;

        List<Comment> commentList = board.getComments();

        int size = 0;
        int sum = 0;

        for(Comment comment : commentList) {
            int score = comment.getScore();
            if(comment.getScore() == 0) continue;

            size++;
            sum += score;
        }

        return (sum * 100) / size;
    }

    @Override
    @Transactional
    public Page<CommentResponse.FindByBoardId> findByBoardId(Long takenBoardId, int takenPage) {
        PageRequest request = PageRequest.of(takenPage - 1, 10, Sort.by("id").descending());
        Page<Comment> commentList = commentRepository.findAllByBoardId(takenBoardId, request);

        return toDtoPage(commentList);
    }

    @Override
    @Transactional
    public CommentResponse.FindComment updateById(Long commentId, Long takenMemberId, CommentRequest.Edit takenDto){
        Comment savedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다"));

        if(!takenMemberId.equals(savedComment.getMember().getId()))
            throw new RestApiException(ErrorCode.USER_MISMATCH);

        savedComment.update(takenDto);
        Comment save = commentRepository.save(savedComment);

        Board savedBoard = savedComment.getBoard();
        int avgScore = getAvgScore(savedBoard);
        savedBoard.avgScorePatch(avgScore);

        boardRepository.save(savedBoard);

        return CommentResponse.FindComment.toDto(save);
    }

    private Page<CommentResponse.FindByBoardId> toDtoPage(Page<Comment> commentList){
        return commentList.map(CommentResponse.FindByBoardId::toDto);
    }
}
