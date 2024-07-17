package com.example.demo.dto.board;

import com.example.demo.entity.Board;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {
    private String title;
    private String body;
    private Integer satisfaction;
}
