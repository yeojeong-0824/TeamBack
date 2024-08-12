package com.example.demo.board.usergreat.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGreatRepository extends JpaRepository<UserGreat, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    List<UserGreat> findAllByBoardId(Long boardId);
}
