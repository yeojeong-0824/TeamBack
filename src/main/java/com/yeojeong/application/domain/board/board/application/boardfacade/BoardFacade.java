package com.yeojeong.application.domain.board.board.application.boardfacade;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import org.springframework.data.domain.Page;

public interface BoardFacade {
    BoardResponse.FindBoard save(BoardRequest.SaveBoard dto, Long memberId);
    BoardResponse.FindBoard updateById(Long id, Long memberId, BoardRequest.PutBoard dto);
    void deleteById(Long id, Long memberId);
    BoardResponse.FindBoard findById(Long id, Long memberId);
    Page<BoardResponse.FindBoardList> findAll(String searchKeyword, String keyword, String sortKeyword, int page);
}
