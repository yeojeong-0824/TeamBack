package com.yeojeong.application.domain.board.board.application.boardservice.implement;

import com.yeojeong.application.autocomplate.application.AutocompleteService;
import com.yeojeong.application.autocomplate.presentation.dto.AutocompleteResponse;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import io.micrometer.core.aop.CountedAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AutocompleteService autocompleteService;

    @Override
    public Board save(Board entity) {
        return boardRepository.save(entity);
    }

    @Override
    public Board update(Board entity) {
        return boardRepository.save(entity);
    }

    @Override
    public void delete(Board entity) {
        boardRepository.delete(entity);
    }

    @Override
    @Cacheable(value = "boards", key = "#id", condition="#id != null")
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<Board> findByMember(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return boardRepository.findAllByMemberId(memberId, pageRequest);
    }

    // 조건에 따른 게시글 검색, 정렬
    @Override
    public Page<Board> findAll(String searchKeyword, String keyword, String sortKeyword, int page) {
        final int pageSize = 10;
        // 생성 날짜
        PageRequest request = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        if (sortKeyword != null) {
            request = switch (sortKeyword) {
                case "score" -> PageRequest.of(page - 1, pageSize, Sort.by("avgScore").descending());
                case "comment" -> PageRequest.of(page - 1, pageSize, Sort.by("commentCount").descending());
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

    @Override
    public void updateComment(Board board) {
        board.avgScorePatch(getAvgScore(board));
        boardRepository.save(board);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        boardRepository.deleteByMemberId(memberId);
    }

    private int getAvgScore(Board board) {
        int commentCount = board.getCommentCount();
        if(commentCount == 0) return 0;

        List<Comment> commentList = board.getComments();

        int size = 0;
        int sum = 0;

        for(Comment comment : commentList) {
            int score = comment.getScore();
            if(comment.getScore() == 0) continue;

            size++;
            sum += score;
        }
        if(size == 0) return 0;
        return (sum * 100) / size;
    }
}
