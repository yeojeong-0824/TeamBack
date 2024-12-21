package com.yeojeong.application.domain.board.comment.application.commentservice.implement;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Comment entity) {
        commentRepository.delete(entity);
    }

    @Override
    public Page<Comment> findByBoardId(Long boardId, int page) {
        final int pageSize = 10;
        PageRequest request = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        return commentRepository.findAllByBoardId(boardId, request);
    }

    @Override
    public List<Comment> findByMemberId(Long memberId) {
        return commentRepository.findAllByMemberId(memberId);
    }

    @Override
    public Comment update(Comment entity) {
        return commentRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByMemberId(Long memberId) {
        commentRepository.deleteByMemberId(memberId);
    }

    @Override
    @Transactional
    public void deleteByBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }
}
