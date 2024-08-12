package com.example.demo.board.usergreat.application;

import com.example.demo.board.usergreat.presentation.dto.UserGreatResponse;

import java.util.List;

public interface UserGreatService {
    void save(Long userId, Long boardId);
    List<UserGreatResponse.UserIdByBoardId> findUserIdByBoardId(Long boardId);
}
