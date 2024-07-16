package com.example.demo.service;

import com.example.demo.dto.board.BoardCreateRequest;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse.*;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    // 게시글 작성
    public Board writeBoard(BoardCreateRequest boardCreateRequest, short age){
        Board board = boardCreateRequest.toEntity(age);
        return boardRepository.save(board);
    }

    // 게시글 수정
    public Board updateBoard(Long boardId, BoardRequest.BoardUpdateRequest boardUpdateRequest){
        Board board = boardRepository.findById(boardId).get();
        board.update(boardUpdateRequest);
        return board;
    }


    // 게시글 삭제
    public Long deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId).get();
        boardRepository.delete(board);
        return boardId;
    }

    // 하나의 게시글
    public BoardReadResponse getBoard(Long boardId){
        Board board = boardRepository.findById(boardId).get();
        return new BoardReadResponse(board);
    }

    // 나의 게시글 보기
    public Page<Board> getAllMyBoardList(String username, PageRequest request){
        return boardRepository.findAllByMemberUsername(username, request);
    }

    // 조건에 따른 게시글 검색, 정렬
    public Page<Board> getSearchBoardList(String searchKeyword, String keyword, String sortKeyword, int page) {
        // 생성 날짜
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        if (sortKeyword != null) {
            request = switch (sortKeyword) {
                case "like" -> PageRequest.of(page - 1, 10, Sort.by("likeCount").descending());
                case "comment" -> PageRequest.of(page - 1, 10, Sort.by("commentCount").descending());
                default -> request;
            };
        }

        if (searchKeyword.equals("title")){
            return boardRepository.findAllByTitleContaining(keyword, request);
        } else if (searchKeyword.equals("body")) {
            return boardRepository.findAllByBodyContaining(keyword, request);
        } else if (searchKeyword != null){
            return boardRepository.findByTitleOrBodyContaining(keyword, request);
        } else {
            return boardRepository.findAll(request);
        }
    }
}
