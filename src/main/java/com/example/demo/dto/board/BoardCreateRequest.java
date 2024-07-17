package com.example.demo.dto.board;

import com.example.demo.entity.Board;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {
    private String title;
    private String body;
    private Integer satisfaction;

    public Board toEntity(Integer age){
        return Board.builder()
                .title(title)
                .body(body)
                .view(0)
                .age(age)
                .satisfaction(satisfaction)
                .likeCount(0)
                .commentCount(0)
                .build();
    }
}
