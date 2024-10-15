package com.yeojeong.application.domain.board.board.application.boardservice;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.member.domain.Member;
import org.springframework.data.domain.Page;

public interface BoardService {
    Board save(Board entity, Member member);
    Board updateById(Long id, Long memberId, Board entity);
    Board findById(Long id);
    Page<Board> findAll(String searchKeyword, String keyword, String sortKeyword, int page);
    void deleteById(Long id, Long memberId);
}
