package com.yeojeong.application.domain.board.board.application.boardfacade.implement;

import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BoardFacadeImplTest {
    @InjectMocks
    private BoardFacadeImpl boardFacadeImpl;

    @Mock
    private BoardService boardService;

    @Mock
    private MemberService memberService;

//    @Test
//    @DisplayName("조회수 동시성 테스트")
//    void test1() throws InterruptedException {
//        Long boardId = 1L;
//
//        Board mockBoard = Board.builder()
//                .id(boardId)
//                .locationName("locationName")
//                .formattedAddress("formattedAddress")
//                .longitude("longitude")
//                .latitude("latitude")
//                .title("title")
//                .body("body")
//                .view(0)
//                .member(new Member())
//                .comments(new ArrayList<Comment>())
//                .avgScore(0)
//                .commentCount(0)
//                .build();
//
//        when(boardService.findById(boardId)).thenReturn(mockBoard);
//        when(boardService.update(any(Board.class))).thenAnswer(data -> mockBoard);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//        for (int i = 0; i <= 100; i++) {
//            executorService.execute(() -> {
//                boardFacadeImpl.findById(boardId);
//            });
//        }
//
//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.MINUTES); // 모든 스레드가 종료될 때까지 대기
//
//        assertEquals(100, mockBoard.getView());
//    }
}