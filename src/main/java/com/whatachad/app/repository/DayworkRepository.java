package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Daywork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayworkRepository extends JpaRepository<Daywork, Long> {

    List<Daywork> findByDaySchedule_Id(Long dayScheduleId, Pageable pageable);
}
