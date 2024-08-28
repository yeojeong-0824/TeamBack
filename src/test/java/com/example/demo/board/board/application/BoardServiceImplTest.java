package com.example.demo.board.board.application;

import com.example.demo.autocomplate.application.AutocompleteService;
import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.board.board.presentation.dto.BoardResponse;
import com.example.demo.config.redis.RedisRepository;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import com.example.demo.member.member.presentation.dto.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.*;




import java.util.concurrent.*;

@SpringBootTest
public class BoardServiceImplTest {
    @Autowired
    private BoardServiceImpl boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private RedisRepository redisRepository;

    @Autowired
    private AutocompleteService autocompleteService;

    @Autowired
    private RedissonClient redissonClient;

    private Board board;

    @BeforeEach
    void setUp() {
        BoardRequest.DefaultBoard dto = BoardRequest.DefaultBoard.builder()
                .locationName("test")
                .formattedAddress("test")
                .latitude("test")
                .longitude("test")
                .title("test")
                .body("tett")
                .build();

        Member member = Member.builder()
                .username("user12")
                .nickname("asdasd")
                .email("test@naber.com")
                .name("test")
                .age(20)
                .password("1q2w3e4r")
                .role(MemberRole.USER.getRole())
                .build();

        Member test = memberRepository.save(member);
        boardService.save(dto, test.getId());
    }

    @Test
    void findById() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    boardService.findById(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        BoardResponse.BoardReadResponse data = boardService.findById(1L, 1L);
        assertThat(threadCount + 1).isEqualTo(data.view());
    }

}
