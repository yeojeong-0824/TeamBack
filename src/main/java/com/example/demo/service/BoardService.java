package com.example.demo.service;

import com.example.demo.config.exception.board.NotFoundBoardException;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    // 게시글 작성
    @Transactional
    public BoardResponse writeBoard(BoardRequest boardRequest, Integer age){
        Board board = boardRequest.toEntity(age);
        boardRepository.save(board);
        return toDto(board);
    }

    // 게시글 수정
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));
        board.update(boardRequest);
        return toDto(board);
    }


    // 게시글 삭제
    @Transactional
    public Long deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));
        boardRepository.delete(board);
        return boardId;
    }

    // 하나의 게시글
    public BoardResponse getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));
        return toDto(board);
    }


    // 조건에 따른 게시글 검색, 정렬
    public Page<BoardResponse> getSearchBoardList(String searchKeyword, String keyword, String sortKeyword, int page) {
        // 생성 날짜
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        if (sortKeyword != null) {
            request = switch (sortKeyword) {
                case "like" -> PageRequest.of(page - 1, 10, Sort.by("likeCount").descending());
                case "comment" -> PageRequest.of(page - 1, 10, Sort.by("commentCount").descending());
                default -> request;
            };
        }

        Page<Board> boardList;
        if (searchKeyword.equals("title")){
            boardList = boardRepository.findAllByTitleContaining(keyword, request);
        } else if (searchKeyword.equals("body")) {
            boardList = boardRepository.findAllByBodyContaining(keyword, request);
        } else if (searchKeyword.equals("null") && keyword != null){
            boardList = boardRepository.findByTitleOrBodyContaining(keyword, request);
        } else {
            boardList = boardRepository.findAll(request);
        }

        return toDtoPage(boardList);

    }

    public BoardResponse toDto(Board board){
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .body(board.getBody())
                .view(board.getView())
                .age(board.getMember().getAge())
                .view(board.getView())
                .satisfaction(board.getSatisfaction())
                .likeCount(board.getLikeCount())
                .memberNickname(board.getMember().getNickname())
                .build();
    }

    public Page<BoardResponse> toDtoPage(Page<Board> boardList){
        return boardList.map(board -> BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .body(board.getBody())
                .view(board.getView())
                .age(board.getMember().getAge())
                .view(board.getView())
                .satisfaction(board.getSatisfaction())
                .likeCount(board.getLikeCount())
                .memberNickname(board.getMember().getNickname())
                .build());
    }
}
