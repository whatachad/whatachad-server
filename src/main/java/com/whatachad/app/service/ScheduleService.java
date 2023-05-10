package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.*;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.vo.AccountDayworkByDay;
import com.whatachad.app.model.vo.DayworkByDay;
import com.whatachad.app.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private static final int DAYS_UNIT = 5;

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

    @Transactional
    public List<DayworkByDay> findDayworksOnSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = callSchedule(scheduleDto);
        List<DaySchedule> daySchedulesOnSchedule = schedule.getDaySchedules();
        return daySchedulesOnSchedule.stream()
                .map(daySchedule -> new DayworkByDay(
                        LocalDate.of(schedule.getYear(), schedule.getMonth(), daySchedule.getDay()),
                        daySchedule.getDayworks()))
                .toList();
    }

    @Transactional
    public Slice<AccountDayworkByDay> findAllOnSchedule(ScheduleDto scheduleDto, Integer page) {
        Schedule schedule = callSchedule(scheduleDto);
        PageRequest pageRequest = PageRequest.of(Objects.requireNonNullElse(page, 0), DAYS_UNIT);
        Slice<DaySchedule> daySchedules = dayScheduleService.findRecentDaySchedules(schedule.getId(), pageRequest);
        return daySchedules.map(daySchedule ->
                new AccountDayworkByDay(LocalDate.of(schedule.getYear(), schedule.getMonth(), daySchedule.getDay()),
                        daySchedule.getAccounts(), daySchedule.getDayworks())
        );
    }

    /**
     * Account Methods
     */
    @Transactional
    public Account createAccountOnSchedule(Integer day, AccountDto accountDto, ScheduleDto scheduleDto) {
        Schedule schedule = callSchedule(scheduleDto);
        DaySchedule daySchedule = dayScheduleService.createAccountOnDay(day, accountDto, schedule.getId());
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
        User loginUser = getLoginUser();
        Optional<Schedule> findSchedule = scheduleRepository.findScheduleOfMonth(
                scheduleDto.getYear(),
                scheduleDto.getMonth(),
                loginUser.getId());
        if (findSchedule.isEmpty()) {
            return scheduleRepository.save(Schedule.create(scheduleDto, loginUser));
        }
        return findSchedule.get();
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
