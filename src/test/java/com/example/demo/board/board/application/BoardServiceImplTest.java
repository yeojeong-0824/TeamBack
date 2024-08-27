package com.example.demo.board.board.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.concurrent.*;

@SpringBootTest
public class BoardServiceImplTest {

    @Autowired
    private BoardServiceImpl boardService;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    private RLock rLock;

    @Test
    void testConcurrentFindById() throws InterruptedException, ExecutionException {
        int threadCount = 80;
        //비동기로 실행되는 작업을 단순화해서 사용하게 도와주는 자바의 api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //카운트다운래치는 다른 스래드에서 수행중인 작업을 완료될때까지 대기하도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        Board board = Board.builder()
                .id(1L)
                .view(0)
                .build();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //검증 프로세스 진행~~
        assertEquals(예상,실제);
    }

}
