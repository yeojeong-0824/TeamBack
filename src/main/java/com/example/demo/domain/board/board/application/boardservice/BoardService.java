package com.example.demo.domain.board.board.application.boardservice;

import com.example.demo.domain.board.board.presentation.dto.BoardRequest;
import com.example.demo.domain.board.board.presentation.dto.BoardResponse;
import com.example.demo.domain.board.board.presentation.dto.GoogleApiResponse;
import org.springframework.data.domain.Page;

public interface BoardService {
    BoardResponse.FindBoard save(BoardRequest.SaveBoard request, Long memberId);
    BoardResponse.FindBoard updateById(Long id, Long memberId, BoardRequest.PutBoard request);
    BoardResponse.FindBoard findById(Long id, Long memberId);
    Page<BoardResponse.FindBoardList> findAllBySearchKeyword(String searchKeyword, String keyword, String sortKeyword, int page);
    void deleteById(Long id, Long memberId);
    GoogleApiResponse getSearchLocation(String textQuery);
}
