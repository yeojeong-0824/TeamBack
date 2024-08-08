package com.example.demo.board.userGreat.application;

import com.example.demo.board.userGreat.presentation.dto.UserGreatRequest;

public interface UserGreatService {
    void save(UserGreatRequest.SaveUserGreat takenDto, Long userId, Long boardId);
}
