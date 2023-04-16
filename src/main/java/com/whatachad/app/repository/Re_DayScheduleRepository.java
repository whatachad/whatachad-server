package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Re_DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Re_DayScheduleRepository extends JpaRepository<Re_DaySchedule, Long> {
    // todo: Re_이름 삭제한 뒤 @Query 삭제하기
    @Query("select d from Re_DaySchedule d where d.date = :date and d.schedule.id = :schedule_id")
    Optional<Re_DaySchedule> findBydateAndSchedule_Id(@Param(value = "date") Integer date, @Param(value = "schedule_id") Long scheduleId);
}
