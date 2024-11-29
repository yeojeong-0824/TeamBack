package com.yeojeong.application.domain.planner.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByPlannerId(Long plannerId);

    @Query("SELECT COUNT(e) > 0 FROM Location e " +
            "WHERE (e.year < :year) OR " +
            "(e.year = :year AND e.month < :month) OR " +
            "(e.year = :year AND e.month = :month AND e.day < :day) OR " +
            "(e.year = :year AND e.month = :month AND e.day = :day AND e.hour < :hour) OR " +
            "(e.year = :year AND e.month = :month AND e.day = :day AND e.hour = :hour AND e.minute < :minute)")
    boolean existsBefore(@Param("year") int year,
                         @Param("month") int month,
                         @Param("day") int day,
                         @Param("hour") int hour,
                         @Param("minute") int minute);

    @Query("SELECT COUNT(e) > 0 FROM Location e " +
            "WHERE (e.year > :year) OR " +
            "(e.year = :year AND e.month > :month) OR " +
            "(e.year = :year AND e.month = :month AND e.day > :day) OR " +
            "(e.year = :year AND e.month = :month AND e.day = :day AND e.hour > :hour) OR " +
            "(e.year = :year AND e.month = :month AND e.day = :day AND e.hour = :hour AND e.minute > :minute)")
    boolean existsAfter(@Param("year") int year,
                        @Param("month") int month,
                        @Param("day") int day,
                        @Param("hour") int hour,
                        @Param("minute") int minute);

    void deleteByPlannerId(Long plannerId);
}
