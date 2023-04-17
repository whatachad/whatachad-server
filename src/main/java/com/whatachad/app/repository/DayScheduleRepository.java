package com.whatachad.app.repository;

import com.whatachad.app.model.domain.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {

    @Query("select d from DaySchedule d where d.date = :date and d.schedule.id = :scheduleId")
    Optional<DaySchedule> findByDateAndScheduleId(@Param("date") Integer date, @Param("scheduleId") Long scheduleId);

}
