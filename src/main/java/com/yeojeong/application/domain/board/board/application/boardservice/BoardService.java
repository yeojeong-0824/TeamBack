package com.yeojeong.application.domain.board.board.application.boardservice;

import com.yeojeong.application.domain.board.board.domain.Board;
import org.springframework.data.domain.Page;

public interface BoardService {
    Board save(Board entity);
    Board update(Board entity, Board updateEntity);
    Board findById(Long id);
    Page<Board> findByMember(Long memberId, int page);
    Page<Board> findAll(String searchKeyword, String keyword, String sortKeyword, int page);
    void delete(Board entity);
    void deleteByMemberId(Long memberId);
}
