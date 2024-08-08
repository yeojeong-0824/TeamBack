package com.example.demo.board.userGreat.application;

import com.example.demo.board.userGreat.domain.UserGreatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGreatServiceImpl implements UserGreatService {

    private final UserGreatRepository userGreatRepository;

    public void save(Long userId, Long boardId) {
//        userGreatRepository.save();
    }

}
