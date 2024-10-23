package com.yeojeong.application.domain.board.board.application.boardfacade.implement;

import com.yeojeong.application.config.exception.RestApiException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.config.util.customannotation.RedisLocker;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardFacadeImpl implements BoardFacade {

    private final BoardService boardService;
    private final MemberService memberService;

    @Override
    @Transactional
    public BoardResponse.FindById save(BoardRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Board entity = BoardRequest.Save.toEntity(dto, member);
        Board savedEntity = boardService.save(entity, member);

        return BoardResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public BoardResponse.FindById update(Long id, Long memberId, BoardRequest.Put dto) {
        Board savedEntity = boardService.findById(id);
        checkMember(savedEntity, memberId);

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

        boardService.delete(savedEntity);
    }

    @Override
    @Transactional
    @RedisLocker(key = "findById")
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
        if (!memberId.equals(board.getMember().getId())) throw new RestApiException(ErrorCode.USER_MISMATCH);
    }
}
