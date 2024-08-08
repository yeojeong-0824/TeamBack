package com.example.demo.board.userGreat.domain;

import com.example.demo.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGreatRepository extends JpaRepository<UserGreat, Long> {

    List<UserGreat> findByMember(Member member);
}
