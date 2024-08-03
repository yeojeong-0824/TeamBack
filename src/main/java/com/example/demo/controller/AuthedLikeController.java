package com.example.demo.controller;

import com.example.demo.entity.Board;
import com.example.demo.service.LikeService;
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

    private final LikeService likeService;

    @GetMapping("/{memberId}")
    public ResponseEntity<List<Board>> getLikedBoardsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(likeService.getLikedBoardsByMember(memberId));
    }

}
