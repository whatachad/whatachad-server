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

    @Query(value = "select count(d.id) > 0 from Daywork d where d.schedule = :schedule")
    boolean existBySchedule(@Param("schedule") Schedule schedule);
}
