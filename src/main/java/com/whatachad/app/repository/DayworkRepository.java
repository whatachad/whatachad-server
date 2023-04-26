package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Daywork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayworkRepository extends JpaRepository<Daywork, Long> {

    @Query("select d from Daywork d where d.daySchedule.id = :dayScheduleId order by d.id asc")
    List<Daywork> findByDayId(@Param("dayScheduleId") Long dayScheduleId);

    @Query("select d from Daywork d where d.daySchedule.id = :dayScheduleId order by d.id asc")
    List<Daywork> findLimitDayworksByDayId(@Param("dayScheduleId") Long dayScheduleId, Pageable pageable);
}
