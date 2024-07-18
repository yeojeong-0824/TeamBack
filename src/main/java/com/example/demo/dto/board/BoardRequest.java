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



    public record BoardSaveRequest(
            String title,
            String body,
            Integer view,
            Integer age,
            Integer satisfaction,
            Integer likeCount,
            Integer commentCount
    ) {
    }

}
