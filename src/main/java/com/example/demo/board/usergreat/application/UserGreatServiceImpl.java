package com.example.demo.board.usergreat.application;

import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.usergreat.domain.UserGreat;
import com.example.demo.board.usergreat.domain.UserGreatRepository;
import com.example.demo.board.usergreat.presentation.dto.UserGreatRequest;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGreatServiceImpl implements UserGreatService {

    private final UserGreatRepository userGreatRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(UserGreatRequest.SaveUserGreat takenDto, Long userId, Long boardId) {
        if(!memberRepository.existsById(userId)) throw new NotFoundDataException("해당 유저가 존재하지 않습니다");
        if(!boardRepository.existsById(boardId)) throw new NotFoundDataException("해당 게시글이 존재하지 않습니다");

        UserGreat saveUserGreat = UserGreatRequest.SaveUserGreat.toEntity(takenDto);
        userGreatRepository.save(saveUserGreat);
    }
}
