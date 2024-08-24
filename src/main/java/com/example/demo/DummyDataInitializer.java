package com.example.demo;

import com.example.demo.board.board.application.BoardServiceImpl;
import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DummyDataInitializer {

    private final BoardServiceImpl boardServiceImpl;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        // 더미 Member 생성
        Member member = Member.builder()
                .username("john_doe")
                .nickname("string")
                .email("john.doe@example.com")
                .password("password")
                .name("John Doe")
                .age(30)
                .role("USER")
                .build();

        memberRepository.save(member);

        // 더미 Board 저장 요청 생성
        BoardRequest.DefaultBoard dummyBoard = new BoardRequest.DefaultBoard(
                "string",  // locationName
                "string",  // formattedAddress
                "string",  // latitude
                "string",  // longitude
                "string",  // title
                "string"   // body
        );

        boardServiceImpl.save(dummyBoard,1L);

        log.info("더미 데이터 초기화 완료: 게시글 저장됨");
    }
}
