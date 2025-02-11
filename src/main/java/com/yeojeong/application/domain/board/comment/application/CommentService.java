package com.yeojeong.application.domain.board.comment.application;

import com.yeojeong.application.config.exception.OwnershipException;
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
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다."));
    }

    public Comment findByIdAuth(Long id, Long memberId) {
        Comment comment = findById(id);
        if(!comment.getMember().getId().equals(memberId)) throw new OwnershipException("댓글을 작성한 회원이 아닙니다.");
        return comment;
    }

    public void delete(Comment entity) {
        commentRepository.delete(entity);
    }

    public void save(Comment entity) {
        commentRepository.save(entity);
    }

    public Page<Comment> findByBoardId(Long boardId, int page) {
        final int pageSize = 10;
        PageRequest request = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        return commentRepository.findAllByBoardId(boardId, request);
    }

    public List<Comment> findByMemberId(Long memberId) {
        return commentRepository.findAllByMemberId(memberId);
    }

    public void update(Comment entity, Comment updateEntity) {
        entity.update(updateEntity);
        commentRepository.save(entity);
    }
}
