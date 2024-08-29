package com.example.demo.board.board.application.boardservice.implement;

import com.example.demo.autocomplate.application.AutocompleteService;
import com.example.demo.autocomplate.presentation.dto.AutocompleteResponse;
import com.example.demo.board.board.application.boardservice.BoardService;
import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.board.board.presentation.dto.BoardResponse;
import com.example.demo.board.board.presentation.dto.GoogleApiRequest;
import com.example.demo.board.board.presentation.dto.GoogleApiResponse;
import com.example.demo.config.exception.AuthorityException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.exception.RequestDataException;
import com.example.demo.config.redis.RedisRepository;
import com.example.demo.config.util.customannotation.RedissonLocker;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
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

    private final RedissonClient redissonClient;

    // 게시글 작성
    @Override
    @Transactional
    public void save(BoardRequest.SaveBoard takenDto, Long takenMemberId) {
        Member member = memberRepository.findById(takenMemberId).
                orElseThrow(() -> new NotFoundDataException("해당 회원을 찾을 수 없습니다."));

        Board board = BoardRequest.SaveBoard.toEntity(takenDto, member);

        Board save = boardRepository.save(board);
        autocompleteService.addAutocomplete(save.getTitle());
    }

    // 게시글 수정
    @Override
    @Transactional
    public void updateById(Long takenBoardId, Long takenMemberId, BoardRequest.PutBoard takenDto) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));

        if (!takenMemberId.equals(board.getMember().getId())) {
            throw new AuthorityException("게시글을 작성한 회원이 아닙니다");
        }

        board.update(takenDto);
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

    // 전체 게시글
    @Override
    public Page<BoardResponse.FindBoardList> findAll(int takenPage) {
        PageRequest request = PageRequest.of(takenPage - 1, 10, Sort.by("id").descending());

        Page<Board> boardList = boardRepository.findAll(request);
        return toDtoPage(boardList);
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

    // 구글맵 api 를 사용한 장소 정보 불러오기
    @Override
    public GoogleApiResponse getSearchLocation(String textQuery) {
        String url = "https://places.googleapis.com/v1/places:searchText";
        String key = "나만의 키 값";

        // 검색할 값
        GoogleApiRequest result = GoogleApiRequest.builder()
                .textQuery(textQuery)
                .includedType("restaurant")
                .languageCode("ko")
                .build();

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Goog-Api-Key", key);
        headers.set("X-Goog-FieldMask", "places.displayName,places.location,places.formattedAddress");  // 내가 받을 정보를 세팅

        // 보낼 바디와 헤더를 세팅
        HttpEntity<GoogleApiRequest> entity = new HttpEntity<>(result, headers);

        // post 로 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responses = restTemplate.postForEntity(url, entity, String.class);

        // 문자열을 dto 로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(responses.getBody(), GoogleApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RequestDataException("구글 API 오브젝트 맵퍼 실패");
        }
    }

    private Page<BoardResponse.FindBoardList> toDtoPage(Page<Board> boardList) {
        return boardList.map(BoardResponse.FindBoardList::toDto);
    }
}
