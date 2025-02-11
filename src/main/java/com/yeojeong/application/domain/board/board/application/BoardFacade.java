package com.yeojeong.application.domain.board.board.application;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.application.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFacade {

    private final BoardService boardService;

    private final MemberService memberService;
    private final PlannerService plannerService;

    @Transactional
    public BoardResponse.FindById save(BoardRequest.BoardSave dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Board entity = BoardRequest.BoardSave.toEntity(dto, member);
        if(dto.plannerId() != 0L) plannerService.findByIdAuth(dto.plannerId(), memberId);

        Board savedEntity = boardService.save(entity);
        return BoardResponse.FindById.toDto(savedEntity);
    }

    public BoardResponse.FindById update(Long id, Long memberId, BoardRequest.BoardPut dto) {
        Board savedEntity = boardService.findByIdAuth(id, memberId);
        if(dto.plannerId() != 0L) plannerService.findByIdAuth(dto.plannerId(), memberId);

        Board updateEntity = BoardRequest.BoardPut.toEntity(dto);
        Board rtnEntity = boardService.update(savedEntity, updateEntity);

        return BoardResponse.FindById.toDto(rtnEntity);
    }

    @Transactional
    public BoardResponse.FindById findByIdForComment(Long id) {
        Board savedEntity = boardService.findById(id);
        List<Comment> comments = savedEntity.getComments();

        int count = 0;
        int sum = 0;
        for(Comment comment : comments) {
            if(comment.getScore() == 0) continue;
            sum += comment.getScore();
            count++;
        }

        if(count == 0) return BoardResponse.FindById.toDto(savedEntity);
        int avgScore = (sum / count) * 100;

        Board rtnEntity = boardService.updateForComment(savedEntity, comments.size(), avgScore);
        return BoardResponse.FindById.toDto(rtnEntity);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Board savedEntity = boardService.findByIdAuth(id, memberId);
        boardService.delete(savedEntity);
    }

    @Transactional
    public BoardResponse.FindById findById(Long id) {
        Board savedEntity = boardService.BoardFindById(id);
        return BoardResponse.FindById.toDto(savedEntity);
    }

    public Page<BoardResponse.FindAll> findAll(String keyword, SortType sortType, int page) {
        Page<Board> savedEntityPage = boardService.findAll(keyword, sortType, page);
        return savedEntityPage.map(BoardResponse.FindAll::toDto);
    }
}
