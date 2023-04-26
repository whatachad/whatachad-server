package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a where a.daySchedule.id = :dayScheduleId order by a.id asc")
    List<Account> findByDayScheduleId(@Param("dayScheduleId") Long dayScheduleId);
}
