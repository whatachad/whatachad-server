package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.*;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final DayScheduleService dayScheduleService;

    /**
     * Schedule Methods
     */
    @Transactional(readOnly = true)
    public Schedule findSchedule(ScheduleDto scheduleDto) {
        return scheduleRepository.findScheduleOfMonth(scheduleDto.getYear(),
                        scheduleDto.getMonth(), getLoginUser().getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));
    }

    @Transactional(readOnly = true)
    public List<List<Daywork>> findDayworksOnSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = callSchedule(scheduleDto);
        List<DaySchedule> daySchedulesOnSchedule = dayScheduleService.findDaySchedulesOnSchedule(schedule.getId());
        List<List<Daywork>> dayworks = new ArrayList<>();
        daySchedulesOnSchedule.stream().forEach(day ->{
            dayworks.add(dayScheduleService.findDayworksOnDay(day.getDay(), schedule.getId()));
        });
        return dayworks;
    }

    /**
     * Account Methods
     */
    @Transactional
    public Account createAccountOnSchedule(Integer date, AccountDto accountDto, ScheduleDto scheduleDto) {
        Schedule schedule = callSchedule(scheduleDto);
        DaySchedule daySchedule = dayScheduleService.createAccountOnDay(date, accountDto, schedule.getId());
        schedule.addDaySchedule(daySchedule);
        Account account = daySchedule.getLastAccount();
        account.setAccountDate(schedule.getYear(), schedule.getMonth(), daySchedule.getDay());
        return account;
    }

    /**
     * Daywork Methods
     */
    @Transactional
    public Daywork createDayworkOnSchedule(Integer day, DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = callSchedule(scheduleDto);
        DaySchedule daySchedule = dayScheduleService.createDayworkOnDay(day, dayworkDto, schedule.getId());
        schedule.addDaySchedule(daySchedule);
        Daywork daywork = daySchedule.getLastDaywork();
        daywork.setDayworkDate(schedule.getYear(), schedule.getMonth(), daySchedule.getDay());
        return daywork;
    }


    /**
     * private Methods
     */
    private Schedule callSchedule(ScheduleDto scheduleDto) {
        Optional<Schedule> findSchedule = scheduleRepository.findScheduleOfMonth(
                scheduleDto.getYear(),
                scheduleDto.getMonth(),
                getLoginUser().getId());
        if (findSchedule.isEmpty()) {
            return scheduleRepository.save(Schedule.create(scheduleDto, getLoginUser()));
        }
        return findSchedule.get();
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
