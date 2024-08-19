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
    @MethodTimer(method = "BoardScoreService.save()")
    public void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenMemberId) {
        Board board = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다"));

        // ToDo: boardScore에서 모든 데이터를 불러 오는 형식은 느리게 동작할 것 같음
        // ToDo: boardScore에서 해당 게시글의 Score 개수만 불러오는 쿼리로 작성해야 할 것 같음
        List<BoardScore> savedEntity = boardScoreRepository.findByBoard(board);
        int size = savedEntity.size();
        int score = (board.getAvgScore() * size) + (takenDto.score() * 100);
        int avg = score / (size + 1);

        board.avgScorePatch(avg);
        boardRepository.save(board);

        BoardScore entity = BoardScoreRequest.SaveScore.toEntity(takenDto, board, member);
        boardScoreRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long takenBoardId, Long takenMemberId) {
        boardScoreRepository.deleteByBoard_IdAndMember_id(takenBoardId, takenMemberId);
    }
}
