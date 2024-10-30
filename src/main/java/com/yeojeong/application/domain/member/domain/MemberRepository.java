package com.yeojeong.application.domain.member.domain;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.lastLoginDate = :date")
    List<Member> findByLastLoginDateBefore(@Param("date") LocalDate date);
}
