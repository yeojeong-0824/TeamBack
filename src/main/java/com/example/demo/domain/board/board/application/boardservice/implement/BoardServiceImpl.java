package com.example.demo.domain.board.board.application.boardservice.implement;

import com.example.demo.autocomplate.application.AutocompleteService;
import com.example.demo.autocomplate.presentation.dto.AutocompleteResponse;
import com.example.demo.domain.board.board.application.boardservice.BoardService;
import com.example.demo.domain.board.board.domain.Board;
import com.example.demo.domain.board.board.domain.BoardRepository;
import com.example.demo.domain.board.board.presentation.dto.BoardRequest;
import com.example.demo.domain.board.board.presentation.dto.BoardResponse;
import com.example.demo.config.exception.AuthorityException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.exception.RequestDataException;
import com.example.demo.config.redis.RedisRepository;
import com.example.demo.config.util.customannotation.RedissonLocker;
import com.example.demo.domain.member.member.domain.Member;
import com.example.demo.domain.member.member.domain.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final RedisRepository redisRepository;
    private final AutocompleteService autocompleteService;

    // 게시글 작성
    @Override
    @Transactional
    public BoardResponse.FindBoard save(BoardRequest.SaveBoard takenDto, Long takenMemberId) {
        Member member = memberRepository.findById(takenMemberId).
                orElseThrow(() -> new NotFoundDataException("해당 회원을 찾을 수 없습니다."));

        Board board = BoardRequest.SaveBoard.toEntity(takenDto, member);

        Board save = boardRepository.save(board);

        autocompleteService.addAutocomplete(save.getTitle());
        return BoardResponse.FindBoard.toDto(save);
    }

    // 게시글 수정
    @Override
    @Transactional
    public BoardResponse.FindBoard updateById(Long takenBoardId, Long takenMemberId, BoardRequest.PutBoard takenDto) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        if (!takenMemberId.equals(board.getMember().getId())) {
            throw new AuthorityException("게시글을 작성한 회원이 아닙니다");
        }

        board.update(takenDto);
        Board save = boardRepository.save(board);
        return BoardResponse.FindBoard.toDto(save);
    }

    // 게시글 삭제
    @Override
    @Transactional
    public void deleteById(Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        if (!takenMemberId.equals(board.getMember().getId())) {
            throw new AuthorityException("게시글을 작성한 회원이 아닙니다");
        }

        redisRepository.deleteViewCount(takenBoardId);
        boardRepository.delete(board);
    }

    // 하나의 게시글
    @Transactional
    @Override
    @RedissonLocker(key = "findBoardById")
    public BoardResponse.FindBoard findById(Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        board.addViewCount();
        boardRepository.save(board);

        return BoardResponse.FindBoard.toDto(board);
    }

    // 조건에 따른 게시글 검색, 정렬
    @Override
    public Page<BoardResponse.FindBoardList> findAllBySearchKeyword(String takenSearchKeyword, String takenKeywork, String takenSortKeyword, int takenPage) {
        // 생성 날짜
        PageRequest request = PageRequest.of(takenPage - 1, 10, Sort.by("id").descending());

        if (takenSortKeyword != null) {
            request = switch (takenSortKeyword) {
                case "score" -> PageRequest.of(takenPage - 1, 10, Sort.by("avgScore").descending());
                case "comment" -> PageRequest.of(takenPage - 1, 10, Sort.by("commentCount").descending());
                default -> request;
            };
        }

        Page<Board> boardList;
        if (takenSearchKeyword.equals("content")) {
            // 제목과 내용에 검색어와 일치하는 게시글 검색
            boardList = boardRepository.findByTitleOrBodyContaining(takenKeywork, request);
        } else if (takenSearchKeyword.equals("location")) {
            // 주소와 가게 이름이 검색어와 일치하는 게시글 검색
            boardList = boardRepository.findByFormattedAddressOrLocationNameContaining(takenKeywork, request);
        } else if (takenSearchKeyword.equals("title")) {
            // 자동완성 기능을 통해 제목 검색
            List<String> autocompleteTitles = autocompleteService.getAutocomplete(takenKeywork).list().stream()
                    .map(AutocompleteResponse.Data::value)
                    .collect(Collectors.toList());
            boardList = boardRepository.findByTitleIn(autocompleteTitles, request);
        } else {
            boardList = boardRepository.findAll(request);
        }

        return toDtoPage(boardList);
    }

    private Page<BoardResponse.FindBoardList> toDtoPage(Page<Board> boardList) {
        return boardList.map(BoardResponse.FindBoardList::toDto);
    }
}
