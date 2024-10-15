package com.yeojeong.application.domain.board.board.application.boardservice.implement;

import com.yeojeong.application.autocomplate.application.AutocompleteService;
import com.yeojeong.application.autocomplate.presentation.dto.AutocompleteResponse;
import com.yeojeong.application.config.exception.RestApiException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AutocompleteService autocompleteService;

    @Override
    @Transactional
    public Board save(Board entity, Member member) {
        Board savedEntity = boardRepository.save(entity);
        return savedEntity;
    }

    @Override
    @Transactional
    public Board updateById(Long id, Long memberId, Board entity) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
        if (!memberId.equals(board.getMember().getId())) throw new RestApiException(ErrorCode.USER_MISMATCH);

        board.update(entity);
        Board savedEntity = boardRepository.save(board);

        return savedEntity;
    }

    @Override
    @Transactional
    public void deleteById(Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
        if (!takenMemberId.equals(board.getMember().getId())) throw new RestApiException(ErrorCode.USER_MISMATCH);

        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public Board findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        return board;
    }

    // 조건에 따른 게시글 검색, 정렬
    @Override
    public Page<Board> findAll(String searchKeyword, String keyword, String sortKeyword, int page) {
        // 생성 날짜
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        if (sortKeyword != null) {
            request = switch (sortKeyword) {
                case "score" -> PageRequest.of(page - 1, 10, Sort.by("avgScore").descending());
                case "comment" -> PageRequest.of(page - 1, 10, Sort.by("commentCount").descending());
                default -> request;
            };
        }

        Page<Board> boardList;
        if (searchKeyword.equals("content")) {
            // 제목과 내용에 검색어와 일치하는 게시글 검색
            boardList = boardRepository.findByTitleOrBodyContaining(keyword, request);
        } else if (searchKeyword.equals("location")) {
            // 주소와 가게 이름이 검색어와 일치하는 게시글 검색
            boardList = boardRepository.findByFormattedAddressOrLocationNameContaining(keyword, request);
        } else if (searchKeyword.equals("title")) {
            // 자동완성 기능을 통해 제목 검색
            List<String> autocompleteTitles = autocompleteService.getAutocomplete(keyword).list().stream()
                    .map(AutocompleteResponse.Data::value)
                    .collect(Collectors.toList());
            boardList = boardRepository.findByTitleIn(autocompleteTitles, request);
        } else {
            boardList = boardRepository.findAll(request);
        }

        return boardList;
    }
}
