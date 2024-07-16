package com.example.demo.dto.board;

import com.example.demo.entity.Board;

public class BoardCreateRequest {
    private String title;
    private String body;
    private short satisfaction;

    public Board toEntity(short age){
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
