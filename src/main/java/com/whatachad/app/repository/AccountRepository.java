package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByDaySchedule_Id(Long dayScheduleId);
}
