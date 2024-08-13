package com.example.demo.board.boardscore.application;

import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.board.boardscore.domain.BoardScoreRepository;
import com.example.demo.board.boardscore.presentation.dto.BoardScoreRequest;
import com.example.demo.board.boardscore.presentation.dto.BoardScoreResponse;
import com.example.demo.config.exception.DuplicatedException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardScoreServiceImpl implements BoardScoreService {

    private final BoardScoreRepository boardScoreRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    @Override
    public void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenUserId) {
        if(!memberRepository.existsById(takenUserId)) throw new NotFoundDataException("해당 유저가 존재하지 않습니다");
        if(!boardRepository.existsById(takenBoardId)) throw new NotFoundDataException("해당 게시글이 존재하지 않습니다");

        if(boardScoreRepository.existsByBoardIdAndUserId(takenBoardId, takenUserId)) throw new DuplicatedException("해당 게시글에 별점을 이미 누르셨습니다");

        BoardScore takenEntity = BoardScoreRequest.SaveScore.toEntity(takenDto, takenBoardId, takenUserId);
        boardScoreRepository.save(takenEntity);
    }

    @Override
    public BoardScoreResponse.BoardScoreByBoardId findUserIdByBoardId(Long boardId) {
        if(!boardRepository.existsById(boardId)) throw new NotFoundDataException("해당 게시글이 존재하지 않습니다");

        List<BoardScore> savedEntityList = boardScoreRepository.findAllByBoardId(boardId);
        BoardScoreResponse.BoardScoreByBoardId savedDto = BoardScoreResponse.BoardScoreByBoardId.toDto(savedEntityList);
        return savedDto;
    }

    @Override
    public void deleteByBoardId(Long takenBoardId) {
        boardScoreRepository.deleteByBoardId(takenBoardId);
    }
}
