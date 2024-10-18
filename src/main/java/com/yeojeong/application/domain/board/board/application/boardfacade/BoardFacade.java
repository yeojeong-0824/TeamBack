package com.yeojeong.application.domain.board.board.application.boardfacade;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import org.springframework.data.domain.Page;

public interface BoardFacade {
    BoardResponse.FindById save(BoardRequest.Save dto, Long memberId);
    BoardResponse.FindById update(Long id, Long memberId, BoardRequest.Put dto);
    void delete(Long id, Long memberId);
    BoardResponse.FindById findById(Long id, Long memberId);
    Page<BoardResponse.FindAll> findAll(String searchKeyword, String keyword, String sortKeyword, int page);
}
