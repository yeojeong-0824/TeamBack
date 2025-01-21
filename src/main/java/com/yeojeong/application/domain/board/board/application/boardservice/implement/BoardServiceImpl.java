package com.yeojeong.application.domain.board.board.application.boardservice.implement;

import com.yeojeong.application.autocomplate.application.AutocompleteService;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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
    public Board updateForComment(Board entity, int commentCount, int avgScore) {
        entity.updateAvgScore(avgScore);
        entity.updateCommentCount(commentCount);
        return boardRepository.save(entity);
    }

    @Override
    public void delete(Board entity) {
        boardRepository.delete(entity);
    }

    @Override
//    @Cacheable(value = "boards", key = "#id", condition="#id != null")
    public Board BoardFindById(Long id) {
        Board entity = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
        entity.addViewCount();

        return boardRepository.save(entity);
    }

    @Override
    public Board findById(Long id) {
        Board entity = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다."));
        return boardRepository.save(entity);
    }

    @Override
    public Page<Board> findByMember(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return boardRepository.findAllByMemberId(memberId, pageRequest);
    }

    @Override
    public Page<Board> findAll(String keyword, SortType sortType, int page) {
        PageRequest request = getSortPage(sortType, page);
        if(request == null) throw new RequestDataException("정렬 값이 잘못됐습니다.");

        if(keyword == null)
            return boardRepository.findAll(request);
        return boardRepository.findByTitleOrBodyContaining(keyword, request);
    }

    private PageRequest getSortPage(SortType sortType, int page) {
        final int pageSize = 10;

        if(sortType == SortType.latest)
            return PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        if(sortType == SortType.score)
            return PageRequest.of(page - 1, pageSize, Sort.by("avgScore").descending());

        if(sortType == SortType.comment)
            return PageRequest.of(page - 1, pageSize, Sort.by("commentCount").descending());

        if(sortType == SortType.view)
            return PageRequest.of(page - 1, pageSize, Sort.by("view").descending());

        return null;
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        boardRepository.deleteByMemberId(memberId);
    }
}
