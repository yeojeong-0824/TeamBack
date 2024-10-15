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
    public BoardResponse.FindBoard save(BoardRequest.SaveBoard dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Board entity = BoardRequest.SaveBoard.toEntity(dto, member);
        Board savedEntity = boardService.save(entity, member);

        return BoardResponse.FindBoard.toDto(savedEntity);
    }

    @Override
    public BoardResponse.FindBoard updateById(Long id, Long memberId, BoardRequest.PutBoard dto) {
        Board entity = BoardRequest.PutBoard.toEntity(dto);
        Board savedEntity = boardService.updateById(id, memberId, entity);

        return BoardResponse.FindBoard.toDto(savedEntity);
    }

    @Override
    public void deleteById(Long id, Long memberId) {
        boardService.deleteById(id, memberId);
    }

    @Override
    public BoardResponse.FindBoard findById(Long id, Long memberId) {
        Board savedEntity = boardService.findById(id);
        return BoardResponse.FindBoard.toDto(savedEntity);
    }

    @Override
    public Page<BoardResponse.FindBoardList> findAll(String searchKeyword, String keyword, String sortKeyword, int page) {
        Page<Board> savedEntityPage = boardService.findAll(searchKeyword, keyword, sortKeyword, page);
        return savedEntityPage.map(BoardResponse.FindBoardList::toDto);
    }
}
