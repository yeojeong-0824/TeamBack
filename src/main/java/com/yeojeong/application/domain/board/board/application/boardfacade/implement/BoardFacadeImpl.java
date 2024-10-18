package com.yeojeong.application.domain.board.board.application.boardfacade.implement;

import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFacadeImpl implements BoardFacade {

    private final BoardService boardService;
    private final MemberService memberService;

    @Override
    public BoardResponse.FindById save(BoardRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Board entity = BoardRequest.Save.toEntity(dto, member);
        Board savedEntity = boardService.save(entity, member);

        return BoardResponse.FindById.toDto(savedEntity);
    }

    @Override
    public BoardResponse.FindById update(Long id, Long memberId, BoardRequest.Put dto) {
        Board savedEntity = boardService.findById(id);
        Board entity = BoardRequest.Put.toEntity(dto);
        Board rtnEntity = boardService.update(savedEntity, memberId, entity);

        return BoardResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public void delete(Long id, Long memberId) {
        Board savedEntity = boardService.findById(id);
        boardService.delete(savedEntity, memberId);
    }

    @Override
    public BoardResponse.FindById findById(Long id, Long memberId) {
        Board savedEntity = boardService.findById(id);
        return BoardResponse.FindById.toDto(savedEntity);
    }

    @Override
    public Page<BoardResponse.FindAll> findAll(String searchKeyword, String keyword, String sortKeyword, int page) {
        Page<Board> savedEntityPage = boardService.findAll(searchKeyword, keyword, sortKeyword, page);
        return savedEntityPage.map(BoardResponse.FindAll::toDto);
    }
}
