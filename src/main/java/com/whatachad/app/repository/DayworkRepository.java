package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayworkRepository extends JpaRepository<Daywork, Long> {

    @Query("select d from Daywork d where d.schedule.id = :scheduleId order by d.dateTime.date ASC")
    List<Daywork> findAllBySchedule(@Param("scheduleId") Long scheduleId);
}
