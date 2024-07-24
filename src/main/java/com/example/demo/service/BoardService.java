package com.example.demo.service;

import com.example.demo.config.exception.board.NotFoundBoardException;
import com.example.demo.config.exception.member.NotFoundMemberException;
import com.example.demo.dto.board.BoardRequest.BoardSaveRequest;
import com.example.demo.dto.board.BoardRequest.BoardUpdateRequest;
import com.example.demo.dto.board.BoardResponse.BoardListResponse;
import com.example.demo.dto.board.BoardResponse.BoardReadResponse;
import com.example.demo.dto.board.BoardResponse.BoardSaveResponse;
import com.example.demo.dto.board.BoardResponse.BoardUpdateResponse;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
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
//    private final RedisRepository redisRepository;


    // 게시글 작성
    @Transactional
    public BoardSaveResponse writeBoard(BoardSaveRequest request){

        Member member = memberRepository.findByUsername(request.memberName()).
                orElseThrow(() -> new NotFoundMemberException("해당 회원을 찾을 수 없습니다."));

        Board board = Board.builder()
                .title(request.title())
                .body(request.body())
                .view(0)
                .age(member.getAge())
                .satisfaction(request.satisfaction())
                .likeCount(0)
                .member(member)
                .commentCount(0)
                .build();

        boardRepository.save(board);
        return new BoardSaveResponse(board);
    }

    // 게시글 수정
    @Transactional
    public BoardUpdateResponse updateBoard(Long boardId, BoardUpdateRequest request){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));

        board.update(request.title(), request.body(), request.satisfaction());

        return new BoardUpdateResponse(board);
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
    public BoardReadResponse getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException("해당 게시글을 찾을 수 없습니다."));

//        long increasesViewCount = redisRepository.incrementViewCount(boardId);
//        board.addViewCount((int) increasesViewCount);
//        boardRepository.save(board);

        return new BoardReadResponse(board);
    }


    // 조건에 따른 게시글 검색, 정렬
    public Page<BoardListResponse> getSearchBoardList(String searchKeyword, String keyword, String sortKeyword, int page) {
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

//    public Board toEntity(BoardSaveRequest request, Member member){
//        return Board.builder()
//                .title(request.title())
//                .body(request.body())
//                .view(0)
//                .age(member.getAge())
//                .member(member)
//                .satisfaction(request.satisfaction())
//                .likeCount(0)
//                .commentCount(0)
//                .build();
//    }

    public Page<BoardListResponse> toDtoPage(Page<Board> boardList){
        return boardList.map(BoardListResponse::new);
    }
}
