package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByYearAndMonthAndUser_Id(@Param(value = "year") Integer year, @Param(value = "month") Integer month, @Param(value = "userId") String userId);
}
