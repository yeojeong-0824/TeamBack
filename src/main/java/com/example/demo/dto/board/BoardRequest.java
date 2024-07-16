package com.example.demo.dto.board;

import com.example.demo.entity.Board;

public class BoardRequest {

    public record BoardUpdateRequest(String title, String body){

    }
}
