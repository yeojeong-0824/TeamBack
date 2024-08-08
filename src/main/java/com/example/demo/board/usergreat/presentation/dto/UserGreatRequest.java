package com.example.demo.board.usergreat.presentation.dto;

import com.example.demo.board.usergreat.domain.UserGreat;

public class UserGreatRequest {
    public record SaveUserGreat(
            Short score
    ) {
        public static UserGreat toEntity(SaveUserGreat dto) {
            return UserGreat.builder()
                    .score(dto.score())
                    .build();
        }
    }
}
