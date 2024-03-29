package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.repository.DayScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DayScheduleService {

    private final DayScheduleRepository dayScheduleRepository;
    private final AccountService accountService;
    private final DayworkService dayworkService;


    @Transactional
    public DaySchedule createAccountOnDay(Integer day, AccountDto accountDto, Long scheduleId) {
        DaySchedule daySchedule = callDaySchedule(day, scheduleId);
        Account account = accountService.createAccount(accountDto);
        daySchedule.addAccount(account);
        return daySchedule;
    }

    @Transactional
    public DaySchedule createDayworkOnDay(Integer day, DayworkDto dayworkDto, Long scheduleId) {
        DaySchedule daySchedule = callDaySchedule(day, scheduleId);
        Daywork daywork = dayworkService.createDaywork(dayworkDto);
        daySchedule.addDaywork(daywork);
        return daySchedule;
    }

    @Transactional(readOnly = true)
    public Slice<DaySchedule> findRecentDaySchedules(Long scheduleId, Pageable pageable) {
        int today = LocalDate.now().getDayOfMonth();
        return dayScheduleRepository.findRecentDaySchedules(scheduleId, today, pageable);
    }

    private DaySchedule callDaySchedule(Integer day, Long scheduleId) {
        Optional<DaySchedule> findDaySchedule = dayScheduleRepository.findDaySchedule(day, scheduleId);
        if (findDaySchedule.isEmpty()) {
            return dayScheduleRepository.save(DaySchedule.create(day));
        }
        return findDaySchedule.get();
    }
}
