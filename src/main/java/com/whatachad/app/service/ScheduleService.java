package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.DayScheduleDto;
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

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final DayScheduleService dayScheduleService;

    @Transactional
    public Schedule getOrCreateSchedule(ScheduleDto scheduleDto) {
//        Re_Schedule findSchedule = scheduleRepository.findByYearAndMonthAndUser_Id(
//                        scheduleDto.getYear(),
//                        scheduleDto.getMonth(),
//                        getLoginUser().getId())
//        .orElse(scheduleRepository.save(Re_Schedule.create(scheduleDto, getLoginUser())));
        Optional<Schedule> findSchedule = scheduleRepository.findByYearAndMonthAndUser_Id(
                        scheduleDto.getYear(),
                        scheduleDto.getMonth(),
                        getLoginUser().getId());

        if (findSchedule.isEmpty()) {
            return scheduleRepository.save(Schedule.create(scheduleDto, getLoginUser()));
        }
        return findSchedule.get();
    }

    @Transactional
    public DaySchedule getDaySchedule(DayScheduleDto dayScheduleDto, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
        DaySchedule daySchedule = dayScheduleService.getOrCreateDaySchedule(dayScheduleDto.getDate(), scheduleId);

        daySchedule.addSchedule(schedule);
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
