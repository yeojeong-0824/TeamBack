package com.example.demo.board.boardscore.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.board.boardscore.domain.BoardScoreRepository;
import com.example.demo.board.boardscore.presentation.dto.BoardScoreRequest;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.util.methodtimer.MethodTimer;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardScoreServiceImpl implements BoardScoreService {

    private final BoardScoreRepository boardScoreRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    @MethodTimer(method = "BoardScoreService.save()")
    public void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다"));

        BoardScore entity = BoardScoreRequest.SaveScore.toEntity(takenDto, board, member);
        boardScoreRepository.save(entity);

        int avg = getBoardScoreAvg(board);
        board.avgScorePatch(avg);

        boardRepository.save(board);
    }

    @Override
    @Transactional
    @MethodTimer(method = "BoardScoreService.save()")
    public void delete(Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));

        boardScoreRepository.deleteByBoard_IdAndMember_id(takenBoardId, takenMemberId);

        int avg = getBoardScoreAvg(board);
        board.avgScorePatch(avg);

        boardRepository.save(board);
    }

    @MethodTimer(method = "음 뭐가 더 빠를까?")
    private int getBoardScoreAvg(Board board) {
        List<BoardScore> savedBoardScore = boardScoreRepository.findByBoard(board);
        if(savedBoardScore.isEmpty()) return 0;

        int size = savedBoardScore.size();
        int sum = savedBoardScore.stream().mapToInt(BoardScore::getScore).sum();
        int rtn = (sum * 100) / size ;
        return rtn;
    }
}
