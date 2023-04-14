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
     * */
    @Transactional
    public Daywork createDayworkOnSchedule(DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        Daywork daywork = dayworkService.createDaywork(dayworkDto);

        daywork.addScheduleInDaywork(schedule);
        return daywork;
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(Integer year, Integer month) {
        return scheduleRepository.findByYMonth(year, month, getLoginUser().getId());
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
     *  Account Methods
     * */
    @Transactional
    public Account createAccountOnSchedule(AccountDto accountDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        Account account = accountService.createAccount(accountDto);

        account.addScheduleInAccount(schedule);
        return account;
    }


    /**
     * private Methods
     * */
    private Schedule getOrCreateSchedule(ScheduleDto scheduleDto) {
        boolean existSchedule = isExistSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        Schedule schedule = null;

        if(existSchedule){
            schedule = findSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        }else {
            schedule = createSchedule(scheduleDto);
        }
        return schedule;
    }

    private Schedule createSchedule(ScheduleDto scheduleDto) {
        return scheduleRepository.save(Schedule.create(getLoginUser(), scheduleDto));
    }

    private boolean isExistSchedule(Integer year, Integer month) {
        return scheduleRepository.existByYMonth(year, month, getLoginUser().getId());
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }

}
