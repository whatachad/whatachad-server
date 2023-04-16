package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Re_Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Re_ScheduleRepository extends JpaRepository<Re_Schedule, Long> {

    Optional<Re_Schedule> findByYearAndMonthAndUser_Id(@Param(value = "year") Integer year, @Param(value = "month") Integer month, @Param(value = "userId") String userId);
}
