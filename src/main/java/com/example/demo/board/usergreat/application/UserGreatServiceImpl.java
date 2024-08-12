package com.example.demo.board.usergreat.application;

import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.usergreat.domain.UserGreat;
import com.example.demo.board.usergreat.domain.UserGreatRepository;
import com.example.demo.board.usergreat.presentation.dto.UserGreatResponse;
import com.example.demo.config.exception.DuplicatedException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGreatServiceImpl implements UserGreatService {

    private final UserGreatRepository userGreatRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(Long userId, Long boardId) {
        if(!memberRepository.existsById(userId)) throw new NotFoundDataException("해당 유저가 존재하지 않습니다");
        if(!boardRepository.existsById(boardId)) throw new NotFoundDataException("해당 게시글이 존재하지 않습니다");

        if(userGreatRepository.existsByBoardIdAndUserId(boardId, userId)) throw new DuplicatedException("해당 게시글의 좋아요를 이미 누르셨습니다");

        UserGreat saveUserGreat = UserGreat.builder()
                .userId(userId)
                .boardId(boardId)
                .build();

        userGreatRepository.save(saveUserGreat);
    }

    @Override
    public List<UserGreatResponse.UserIdByBoardId> findUserIdByBoardId(Long boardId) {
        if(!boardRepository.existsById(boardId)) throw new NotFoundDataException("해당 게시글이 존재하지 않습니다");

        List<UserGreat> savedEntity = userGreatRepository.findAllByBoardId(boardId);
        List<UserGreatResponse.UserIdByBoardId> rtn = savedEntity.stream().map(UserGreatResponse.UserIdByBoardId::toDto).toList();

        return rtn;
    }
}
