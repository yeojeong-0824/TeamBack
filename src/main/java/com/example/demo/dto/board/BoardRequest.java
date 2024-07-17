package com.example.demo.dto.board;

import com.example.demo.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    @NotBlank @Size(min = 5)
    private String title;

    @NotBlank @Size(min = 10)
    private String body;

    @NotBlank @Size(max = 10)
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
