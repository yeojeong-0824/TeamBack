package com.yeojeong.application.domain.board.board.application.boardservice.implement;

import com.yeojeong.application.autocomplate.application.AutocompleteService;
import com.yeojeong.application.autocomplate.presentation.dto.AutocompleteResponse;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.config.util.customannotation.RedisLocker;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Board update(Board entity, Board updateEntity) {
        entity.update(updateEntity);
        return boardRepository.save(entity);
    }

    @Override
    public void delete(Board entity) {
        boardRepository.delete(entity);
    }

    @Override
    @RedisLocker(key = "findById", value = "#id")
//    @Cacheable(value = "boards", key = "#id", condition="#id != null")
    public Board findById(Long id) {
        Board entity = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
        entity.addViewCount();
        return boardRepository.save(entity);
    }

    @Override
    public Page<Board> findByMember(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return boardRepository.findAllByMemberId(memberId, pageRequest);
    }

    // 조건에 따른 게시글 검색, 정렬
    @Override
    public Page<Board> findAll(String searchKeyword, String keyword, SortType sortType, int page) {
        PageRequest request = createPageRequest(sortType, page);
        if(request == null) throw new RequestDataException("정렬 값이 잘못되었습니다.");

        Page<Board> boardList;
        if (searchKeyword.equals("content")) {
            boardList = boardRepository.findByTitleOrBodyContaining(keyword, request);
        } else if (searchKeyword.equals("location")) {
            boardList = boardRepository.findByFormattedAddressOrLocationNameContaining(keyword, request);
        } else if (searchKeyword.equals("title")) {
            List<String> autocompleteTitles = autocompleteService.getAutocomplete(keyword).list().stream()
                    .map(AutocompleteResponse.Data::value)
                    .collect(Collectors.toList());
            boardList = boardRepository.findByTitleIn(autocompleteTitles, request);
        } else {
            boardList = boardRepository.findAll(request);
        }

        return boardList;
    }

    private PageRequest createPageRequest(SortType sortType, int page) {
        final int pageSize = 10;

        if(sortType == SortType.latest)
            return PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        if(sortType == SortType.score)
            return PageRequest.of(page - 1, pageSize, Sort.by("avgScore").descending());

        if(sortType == SortType.comment)
            return PageRequest.of(page - 1, pageSize, Sort.by("commentCount").descending());

        return null;
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        boardRepository.deleteByMemberId(memberId);
    }
}
