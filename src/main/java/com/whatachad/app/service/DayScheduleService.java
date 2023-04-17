package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.repository.DayScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DayScheduleService {

    private final DayScheduleRepository dayScheduleRepository;
    private final AccountService accountService;


    @Transactional
    public DaySchedule createAccountOnDay(Integer date, AccountDto accountDto, Long scheduleId) {
        DaySchedule daySchedule = callDaySchedule(date, scheduleId);
        Account account = accountService.createAccount(accountDto);
        daySchedule.addAccount(account);
        return daySchedule;
    }

    @Transactional(readOnly = true)
    public DaySchedule findDaySchedule(Integer date, Long scheduleId) {
        return dayScheduleRepository.findByDateAndScheduleId(date, scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
    }


    private DaySchedule callDaySchedule(Integer date, Long scheduleId) {
        Optional<DaySchedule> findDaySchedule = dayScheduleRepository.findByDateAndScheduleId(date, scheduleId);
        if (findDaySchedule.isEmpty()) {
            return dayScheduleRepository.save(DaySchedule.createByDate(date));
        }
        return findDaySchedule.get();
    }

}
