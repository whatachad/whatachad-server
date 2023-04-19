package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.repository.DayScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DaySchedule createAccountOnDay(Integer date, AccountDto accountDto, Long scheduleId) {
        DaySchedule daySchedule = callDaySchedule(date, scheduleId);
        Account account = accountService.createAccount(accountDto);
        daySchedule.addAccount(account);
        return daySchedule;
    }

    @Transactional
    public Daywork createDayworkOnDay(DayworkDto dayworkDto, Long dayScheduleId) {
        // TODO : merge 후 수정해야 함
        DaySchedule daySchedule =  dayScheduleRepository.findById(dayScheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));

        Daywork daywork = dayworkService.createDaywork(dayworkDto);
        daySchedule.addDaywork(daywork);
        return daywork;
    }

    @Transactional(readOnly = true)
    public List<DaySchedule> findDaySchedulesOnSchedule(Long scheduleId) {
        return dayScheduleRepository.findBySchedule_IdOrderByDateAsc(scheduleId);
    }

    private DaySchedule callDaySchedule(Integer date, Long scheduleId) {
        Optional<DaySchedule> findDaySchedule = dayScheduleRepository.findByDateAndSchedule_Id(date, scheduleId);
        return dayScheduleRepository.findByDateAndSchedule_Id(date, scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
    }

    // TODO : merge 후 수정해야 함
    @Transactional
    public DaySchedule getOrCreateDaySchedule(Integer date, Long scheduleId) {
        Optional<DaySchedule> findDaySchedule = dayScheduleRepository.findByDateAndSchedule_Id(date, scheduleId);

        if (findDaySchedule.isEmpty()) {
            return dayScheduleRepository.save(DaySchedule.createByDate(date));
        }
        return findDaySchedule.get();
    }
}
