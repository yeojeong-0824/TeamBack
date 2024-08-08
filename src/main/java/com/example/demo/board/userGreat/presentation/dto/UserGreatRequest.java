package com.example.demo.board.userGreat.presentation.dto;

import com.example.demo.board.userGreat.domain.UserGreat;
import lombok.Setter;

public class UserGreatRequest {
    @Setter
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
