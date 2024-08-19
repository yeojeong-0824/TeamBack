package com.example.demo.board.board.application;

import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.board.board.presentation.dto.BoardResponse;
import org.springframework.data.domain.Page;

public interface BoardService {
    void save(BoardRequest.DefaultBoard request, Long memberId);
    void updateById(Long id, Long memberId, BoardRequest.BoardUpdateRequest request);
    BoardResponse.BoardReadResponse findById(Long id);
    Page<BoardResponse.BoardListResponse> findAll(int page);
    Page<BoardResponse.BoardListResponse> findAllBySearchKeyword(String searchKeyword, String keyword, String sortKeyword, int page);
    void deleteById(Long id, Long memberId);
}
