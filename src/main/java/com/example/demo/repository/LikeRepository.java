package com.example.demo.repository;

import com.example.demo.entity.Like;
import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByMember(Member member);
}
