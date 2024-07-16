package com.example.demo.dto.board;

import com.example.demo.entity.Board;

public class BoardResponse {

    public record BoardReadResponse(
            String title,
            String body,
            Integer view,
            short age,
            short satisfaction,
            Integer like,
            Integer commentCount){
        public BoardReadResponse(Board board){
            this (
                    board.getTitle(),
                    board.getBody(),
                    board.getView(),
                    board.getAge(),
                    board.getSatisfaction(),
                    board.getLikeCount(),
                    board.getCommentCount()
            );
        }
    }
}