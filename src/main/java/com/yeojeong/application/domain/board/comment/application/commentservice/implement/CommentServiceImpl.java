package com.yeojeong.application.domain.board.comment.application.commentservice.implement;

import com.yeojeong.application.config.exception.RestApiException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.domain.CommentRepository;
import com.yeojeong.application.config.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_COMMENT));
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
    public Page<Comment> findByMemberId(Long memberId, int page) {
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return commentRepository.findAllByMemberId(memberId, request);
    }

    @Override
    @Transactional
    public Comment update(Comment entity, Long memberId, Comment updateEntity) {
        if(!memberId.equals(entity.getMember().getId()))
            throw new RestApiException(ErrorCode.USER_MISMATCH);

        entity.update(updateEntity);
        return commentRepository.save(entity);
    }
}