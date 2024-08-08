package com.example.demo.board.like.presentation;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.like.application.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/like/authed")
public class AuthedLikeController {

    private final LikeServiceImpl likeServiceImpl;

    @GetMapping("/{memberId}")
    public ResponseEntity<List<Board>> getLikedBoardsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(likeServiceImpl.getLikedBoardsByMember(memberId));
    }

}
