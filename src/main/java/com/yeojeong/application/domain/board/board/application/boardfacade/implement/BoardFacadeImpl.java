package com.yeojeong.application.domain.board.board.application.boardfacade.implement;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.config.util.customannotation.RedisLocker;
import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardFacadeImpl implements BoardFacade {

    private final BoardService boardService;
    private final CommentService commentService;

    private final MemberService memberService;
    private final PlannerService plannerService;

    @Override
    @Transactional
    public BoardResponse.FindById save(BoardRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        if(!(dto.plannerId() == null)) plannerService.findById(dto.plannerId());

        Board entity = BoardRequest.Save.toEntity(dto, member);
        Board savedEntity = boardService.save(entity);

        return BoardResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public BoardResponse.FindById update(Long id, Long memberId, BoardRequest.Put dto) {
        Board savedEntity = boardService.findById(id);
        checkMember(savedEntity, memberId);
        if(!(dto.plannerId() == null)) plannerService.findById(dto.plannerId());

        Board entity = BoardRequest.Put.toEntity(dto);
        savedEntity.update(entity);

        Board rtnEntity = boardService.update(savedEntity);
        return BoardResponse.FindById.toDto(rtnEntity);
    }

    @Override
    @Transactional
    public void delete(Long id, Long memberId) {
        Board savedEntity = boardService.findById(id);
        checkMember(savedEntity, memberId);

        commentService.deleteByBoardId(id);
        boardService.delete(savedEntity);
    }

    @Override
    @Transactional
    @RedisLocker(key = "findById", value = "#id")
    public BoardResponse.FindById findById(Long id) {
        Board savedEntity = boardService.findById(id);
        savedEntity.addViewCount();
        Board rtnEntity = boardService.update(savedEntity);
        return BoardResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public Page<BoardResponse.FindAll> findAll(String searchKeyword, String keyword, String sortKeyword, int page) {
        Page<Board> savedEntityPage = boardService.findAll(searchKeyword, keyword, sortKeyword, page);
        return savedEntityPage.map(BoardResponse.FindAll::toDto);
    }

    private void checkMember(Board board, Long memberId) {
        if (!memberId.equals(board.getMember().getId())) throw new OwnershipException("게시글을 작성한 사용자가 아닙니다.");
    }
}
