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
    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    @Override
    public void delete(Comment entity) {
        commentRepository.delete(entity);
    }

    @Override
    public Page<Comment> findByBoardId(Long boardId, int page) {
        int pageSize = 10;
        PageRequest request = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        return commentRepository.findAllByBoardId(boardId, request);
    }

    @Override
    public Page<Comment> findByMemberId(Long memberId, int page) {
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return commentRepository.findAllByMemberId(memberId, request);
    }

    @Override
    public Comment update(Comment entity) {
        return commentRepository.save(entity);
    }
}
