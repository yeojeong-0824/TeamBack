package com.example.demo.board.usergreat.application;

import com.example.demo.board.usergreat.presentation.dto.UserGreatRequest;

public interface UserGreatService {
    void save(UserGreatRequest.SaveUserGreat takenDto, Long userId, Long boardId);
}
