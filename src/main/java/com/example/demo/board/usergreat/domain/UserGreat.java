package com.example.demo.board.usergreat.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGreat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long boardId;
    private Short score;

    public void saveUserIdAndBoardId(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }
}
