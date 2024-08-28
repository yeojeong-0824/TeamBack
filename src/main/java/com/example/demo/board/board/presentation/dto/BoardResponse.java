package com.example.demo.board.board.presentation.dto;

import com.example.demo.board.board.domain.Board;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String locationName;
    private String formattedAddress;
    private String latitude;  // 위도
    private String longitude;  // 경도
    private String title;
    private String body;
    private Integer view;
    private Integer likeCount;

    public record BoardSaveResponse(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body,
            Integer view
    ) {
        public BoardSaveResponse(Board board) {
            this(
                    board.getLocationName(),
                    board.getFormattedAddress(),
                    board.getLatitude(),
                    board.getLongitude(),
                    board.getTitle(),
                    board.getBody(),
                    board.getView()
            );
        }
    }

    public record BoardUpdateResponse(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body,
            Integer view
    ) {
        public BoardUpdateResponse(Board board) {
            this(
                    board.getLocationName(),
                    board.getFormattedAddress(),
                    board.getLatitude(),
                    board.getLongitude(),
                    board.getTitle(),
                    board.getBody(),
                    board.getView()
            );
        }
    }

    public record BoardListResponse(
            Long id,
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            Integer view,
            Integer avgScore,
            Integer commentCount
    ) {
        public BoardListResponse(Board board) {
            this(
                    board.getId(),
                    board.getLocationName(),
                    board.getFormattedAddress(),
                    board.getLatitude(),
                    board.getLongitude(),
                    board.getTitle(),
                    board.getView(),
                    board.getAvgScore(),
                    board.getCommentCount()
            );
        }
    }

    public record BoardReadResponse(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body,  // Body 변수명을 소문자로 변경
            Integer view,
            Integer avgScore,
            MemberInfo member
    ) {
        @Builder
        public record MemberInfo (
                Long userId,
                Integer age,
                String nickname
        ){}

        public BoardReadResponse(Board board) {
            this(
                    board.getLocationName(),
                    board.getFormattedAddress(),
                    board.getLatitude(),
                    board.getLongitude(),
                    board.getTitle(),
                    board.getBody(),
                    board.getView(),
                    board.getAvgScore(),
                    MemberInfo.builder()
                            .userId(board.getMember().getId())
                            .age(board.getMember().getAge())
                            .nickname(board.getMember().getNickname())
                            .build()
            );
        }
    }


}
