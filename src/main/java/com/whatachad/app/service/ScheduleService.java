package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.*;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayScheduleDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final DayScheduleService dayScheduleService;
    private final DayworkService dayworkService;


    /**
     * Daywork Methods
     */
    @Transactional
    public Daywork createDayworkOnSchedule(DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        Daywork daywork = dayworkService.createDaywork(dayworkDto);
        //== (연관관계 맺는 로직 생략) ==//
        return daywork;
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));
    }

    @Transactional
    public Schedule getOrCreateSchedule(ScheduleDto scheduleDto) {
        Optional<Schedule> findSchedule = scheduleRepository.findByYearAndMonthAndUser_Id(
                scheduleDto.getYear(),
                scheduleDto.getMonth(),
                getLoginUser().getId());
        if (findSchedule.isEmpty()) {
            return scheduleRepository.save(Schedule.create(scheduleDto, getLoginUser()));
        }
        return findSchedule.get();
    }
    /**
     * Account Methods
     */
    @Transactional
    public Account createAccountOnSchedule(Integer date, AccountDto accountDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        DaySchedule daySchedule = dayScheduleService.createAccountOnDay(date, accountDto, schedule.getId());
        schedule.addDaySchedule(daySchedule);
        Account account = daySchedule.getLastAccount();
        account.setAccountDate(schedule.getYear(), schedule.getMonth(), daySchedule.getDate());
        return account;
    }

    /**
     * private Methods
     */

    @Transactional
    public DaySchedule getDaySchedule(DayScheduleDto dayScheduleDto, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
        DaySchedule daySchedule = dayScheduleService.getOrCreateDaySchedule(dayScheduleDto.getDate(), scheduleId);

        schedule.addDaySchedule(daySchedule);
        return daySchedule;
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(ScheduleDto scheduleDto) {
        return scheduleRepository.findByYearAndMonthAndUser_Id(scheduleDto.getYear(), scheduleDto.getMonth(), getLoginUser().getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));
    }

    /**
     *  private method
     * */
    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
