package com.example.demo.board.usergreat.presentation.dto;

import com.example.demo.board.usergreat.domain.UserGreat;
import lombok.Builder;

public class UserGreatResponse {
    @Builder
    public record UserIdByBoardId (
            Long userId
    ){
        public static UserIdByBoardId toDto(UserGreat entity) {
            return UserIdByBoardId.builder()
                    .userId(entity.getUserId())
                    .build();
        }
    }
}
