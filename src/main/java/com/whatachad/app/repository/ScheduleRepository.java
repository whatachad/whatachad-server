package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s where s.year = :year and s.month = :month and s.user.id = :userId")
    Optional<Schedule> findScheduleOfMonth(@Param(value = "year") Integer year,
                                           @Param(value = "month") Integer month,
                                           @Param(value = "userId") String userId);

    @Query("select s from Schedule s" +
            " join fetch s.daySchedules d" +
            " join fetch s.user u" +
            " where s.year = :year and s.month = :month and d.day = :day and s.user.id in :followingsIds")
    List<Schedule> findFollowingTodaySchedule(@Param("year")int year,
                                              @Param("month")int monthValue,
                                              @Param("day")int day,
                                              @Param("followingsIds") List<String> followingsIds);
}
