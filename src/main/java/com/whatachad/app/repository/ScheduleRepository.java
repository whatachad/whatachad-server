package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "select count(s.id) > 0 from Schedule s where s.year = :year and s.month = :month and s.user.id = :user_id")
    boolean existByYMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("user_id")String user_id);

    @Query(value = "select s from Schedule s where s.year = :year and s.month = :month and s.user.id = :user_id")
    Schedule findByYMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("user_id")String user_id);
    
}