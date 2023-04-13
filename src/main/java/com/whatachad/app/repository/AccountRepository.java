package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select count(a.id) > 0 from Account a where a.schedule.id = :schedule_id")
    boolean existBySchedule(@Param("schedule_id") Long schedule_id);
}
