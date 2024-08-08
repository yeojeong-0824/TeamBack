package com.example.demo.board.userGreat.application;

import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.userGreat.domain.UserGreatRepository;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGreatServiceImpl implements UserGreatService {

    private final UserGreatRepository userGreatRepository;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void save(Long userId, Long boardId) {
    }

}
