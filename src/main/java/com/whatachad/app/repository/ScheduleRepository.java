package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "select count(s.id) > 0 from Schedule s where s.year = :year and s.month = :month and s.user.id = :userId")
    boolean existByYMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("userId")String userId);

    @Query(value = "select s from Schedule s where s.year = :year and s.month = :month and s.user.id = :userId")
    Optional<Schedule> findByYearMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("userId")String userId);
    
}