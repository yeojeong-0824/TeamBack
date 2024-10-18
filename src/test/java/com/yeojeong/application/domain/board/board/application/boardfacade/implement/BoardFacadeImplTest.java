package com.yeojeong.application.domain.board.board.application.boardfacade.implement;

import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import jakarta.persistence.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardFacadeImplTest {
    @InjectMocks
    private BoardFacadeImpl boardFacadeImpl;

    @Mock
    private BoardService boardService;

    @Mock
    private MemberService memberService;

    @Test
    @DisplayName("조회수 동시성 테스트")
    void test1() {
        Long boardId = 1L;
        Long memberId = 1L;

        Board mockBoard = Board.builder()
                .id(boardId)
                .locationName("locationName")
                .formattedAddress("formattedAddress")
                .longitude("longitude")
                .latitude("latitude")
                .title("title")
                .body("body")
                .view(0)
                .member(new Member())
                .comments(new ArrayList<Comment>())
                .avgScore(0)
                .commentCount(0)
                .build();

        when(boardService.findById(boardId)).thenReturn(mockBoard);
        BoardResponse.FindById response = boardFacadeImpl.findById(boardId, memberId);
        assertNotNull(response);
    }
}