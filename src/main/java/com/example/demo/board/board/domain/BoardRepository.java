package com.example.demo.board.board.domain;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findAllByBodyContaining(String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.body LIKE %:keyword%")
    Page<Board> findByTitleOrBodyContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.formattedAddress LIKE %:keyword% OR b.locationName LIKE %:keyword%")
    Page<Board> findByFormattedAddressOrLocationNameContaining(@Param("keyword") String keyword, Pageable pageable);
}
