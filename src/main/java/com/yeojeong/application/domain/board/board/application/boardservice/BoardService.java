package com.yeojeong.application.domain.board.board.application.boardservice;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import org.springframework.data.domain.Page;

public interface BoardService {
    Board save(Board entity);
    Board update(Board entity, Board updateEntity);
    Board updateForComment(Board entity, int commentCount, int avgScore);

    Board BoardFindById(Long id);
    Board findById(Long id);

    Page<Board> findByMember(Long memberId, int page);
    Page<Board> findAll(String keyword, SortType sortType, int page);

    void delete(Board entity);
    void deleteByMemberId(Long memberId);
}
