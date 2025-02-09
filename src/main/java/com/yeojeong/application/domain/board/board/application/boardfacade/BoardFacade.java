package com.yeojeong.application.domain.board.board.application.boardfacade;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import org.springframework.data.domain.Page;

public interface BoardFacade {
    BoardResponse.FindById save(BoardRequest.BoardSave dto, Long memberId);
    BoardResponse.FindById update(Long id, Long memberId, BoardRequest.BoardPut dto);
    BoardResponse.FindById findByIdForComment(Long id);
    void delete(Long id, Long memberId);
    BoardResponse.FindById findById(Long id);
    Page<BoardResponse.FindAll> findAll(String keyword, SortType sortType, int page);
}
