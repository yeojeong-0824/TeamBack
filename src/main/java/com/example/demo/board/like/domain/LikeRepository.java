package com.example.demo.board.like.domain;

import com.example.demo.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByMember(Member member);
}
