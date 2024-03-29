package com.whatachad.app.repository;

import com.whatachad.app.model.domain.DaySchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {

    @Query("select d from DaySchedule d where d.day = :day and d.schedule.id = :scheduleId")
    Optional<DaySchedule> findDaySchedule(@Param(value = "day") Integer day, @Param(value = "scheduleId") Long scheduleId);

    @Query("select d from DaySchedule d where d.schedule.id = :scheduleId and d.day <= :today order by d.day desc")
    Slice<DaySchedule> findRecentDaySchedules(@Param("scheduleId") Long scheduleId,
                                              @Param("today") int today, Pageable pageable);
}
