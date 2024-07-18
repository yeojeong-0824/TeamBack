package com.example.demo.dto.board;

import com.example.demo.entity.Board;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String body;
    private Integer view;
    private Integer age;
    private Integer satisfaction;
    private Integer likeCount;
    private String memberNickname;

    public record BoardSaveResponse(
            String title,
            String body,
            Integer view,
            Integer age,
            Integer satisfaction,
            Integer likeCount,
            String memberNickname
    ) {
        public BoardSaveResponse(Board board) {
            this(
                    board.getTitle(),
                    board.getBody(),
                    board.getView(),
                    board.getAge(),
                    board.getSatisfaction(),
                    board.getLikeCount(),
                    board.getMember().getNickname()
            );
        }
    }

    public record BoardListResponse(
            Long id,
            String title,
            Integer view,
            Integer satisfaction,
            Integer likeCount,
            String memberNickname
    ) {
        public BoardListResponse(Board board) {
            this(
                    board.getId(),
                    board.getTitle(),
                    board.getView(),
                    board.getSatisfaction(),
                    board.getLikeCount(),
                    board.getMember().getNickname()
            );
        }
    }

    public record BoardReadResponse(
            String title,
            String Body,
            Integer view,
            Integer age,
            Integer satisfaction,
            Integer likeCount,
            String memberNickname
    ) {
        public BoardReadResponse(Board board) {
            this(
                    board.getTitle(),
                    board.getBody(),
                    board.getView(),
                    board.getAge(),
                    board.getSatisfaction(),
                    board.getLikeCount(),
                    board.getMember().getNickname()
            );
        }
    }

}
