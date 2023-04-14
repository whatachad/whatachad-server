package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final DayworkService dayworkService;
    private final AccountService accountService;


    /**
     * Daywork Methods
     */
    @Transactional
    public Daywork createDayworkOnSchedule(DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        Daywork daywork = dayworkService.createDaywork(dayworkDto);

        daywork.addScheduleInDaywork(schedule);
        return daywork;
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(Integer year, Integer month) {
        return scheduleRepository.findByYearMonth(year, month, getLoginUser().getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    /**
     * Account Methods
     */
    @Transactional
    public Account createAccountOnSchedule(AccountDto accountDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        Account account = accountService.createAccount(accountDto);

        account.addScheduleInAccount(schedule);
        return account;
    }

    /**
     * Schedule Method
     */
    @Transactional(readOnly = true)
    public List<Daywork> getDayworksBySchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "schedule"));

        LocalDate lastDayOfMonth = LocalDate.of(schedule.getYear(), schedule.getMonth(), 1).plusMonths(1).minusDays(1);
        final Integer START_DATE = 1;
        final Integer END_DATE = lastDayOfMonth.getDayOfMonth();

        List<Daywork> dayworksOfMonth = dayworkService.findDayworkBySchedule(schedule.getId());
        if (dayworksOfMonth.isEmpty()) return dayworksOfMonth;

        List<Daywork> filterDayworks = new ArrayList<>();
        IntStream.rangeClosed(START_DATE, END_DATE).forEach(currentDate -> {
            dayworksOfMonth.stream()
                    .filter(daywork -> daywork.getDateTime().getDate() == currentDate)
                    .limit(3)
                    .forEach(filterDayworks::add);
        });

        return filterDayworks;
    }

    /**
     * private Methods
     */
    private Schedule getOrCreateSchedule(ScheduleDto scheduleDto) {
        boolean existSchedule = existSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        Schedule schedule = null;

        if (existSchedule) {
            schedule = findSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        } else {
            schedule = createSchedule(scheduleDto);
        }
        return schedule;
    }

    private Schedule createSchedule(ScheduleDto scheduleDto) {
        return scheduleRepository.save(Schedule.create(getLoginUser(), scheduleDto));
    }

    private boolean existSchedule(Integer year, Integer month) {
        return scheduleRepository.existByYMonth(year, month, getLoginUser().getId());
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }

}
