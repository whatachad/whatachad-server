package com.whatachad.app.repository;

import com.whatachad.app.model.domain.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {

    Optional<DaySchedule> findBydateAndSchedule_Id(@Param(value = "date") Integer date, @Param(value = "schedule_id") Long scheduleId);

    List<DaySchedule> findBySchedule_IdOrderByDateAsc(Long scheduleId);
}
