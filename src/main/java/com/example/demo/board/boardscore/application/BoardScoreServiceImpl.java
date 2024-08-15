package com.example.demo.board.boardscore.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.board.boardscore.domain.BoardScoreRepository;
import com.example.demo.board.boardscore.presentation.dto.BoardScoreRequest;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardScoreServiceImpl implements BoardScoreService {

    private final BoardScoreRepository boardScoreRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenUserId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(takenUserId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다"));

        BoardScore entity = BoardScoreRequest.SaveScore.toEntity(takenDto, board, member);
        boardScoreRepository.save(entity);
    }
}
