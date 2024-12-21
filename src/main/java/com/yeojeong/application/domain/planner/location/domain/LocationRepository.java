package com.yeojeong.application.domain.planner.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.member.id = :memberId AND l.unixTime BETWEEN :start AND :end ORDER BY l.unixTime")
    List<Location> findByMemberAndDate(@Param("memberId") Long memberId, @Param("start") Long start, @Param("end") Long end);

    void deleteByMemberId(Long memberId);
    void deleteByPlannerId(Long plannerId);
    List<Location> findByPlannerId(Long plannerId);
}
