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

    private final CommentRepository commentRepository;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다"));
    }

    @Override
    @Transactional
    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Comment entity, Long memberId){
        if(memberId.equals(entity.getMember().getId())) throw new RestApiException(ErrorCode.USER_MISMATCH);
        commentRepository.delete(entity);
    }

    @Override
    @Transactional
    public Page<Comment> findByBoardId(Long boardId, int page) {
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return commentRepository.findAllByBoardId(boardId, request);
    }

    @Override
    @Transactional
    public Comment updateById(Comment entity, Long memberId, Comment updateEntity) {
        if(!memberId.equals(entity.getMember().getId()))
            throw new RestApiException(ErrorCode.USER_MISMATCH);

        entity.update(updateEntity);
        return commentRepository.save(entity);
    }
}
