package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Re_DaySchedule;
import com.whatachad.app.model.domain.Re_Schedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.Re_DayScheduleDto;
import com.whatachad.app.model.dto.Re_ScheduleDto;
import com.whatachad.app.repository.Re_ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Re_ScheduleService {

    private final UserService userService;
    private final Re_ScheduleRepository scheduleRepository;
    private final Re_DayScheduleService dayScheduleService;

    @Transactional
    public Re_Schedule getOrCreateSchedule(Re_ScheduleDto scheduleDto) {
//        Re_Schedule findSchedule = scheduleRepository.findByYearAndMonthAndUser_Id(
//                        scheduleDto.getYear(),
//                        scheduleDto.getMonth(),
//                        getLoginUser().getId())
//        .orElse(scheduleRepository.save(Re_Schedule.create(scheduleDto, getLoginUser())));
        Optional<Re_Schedule> findSchedule = scheduleRepository.findByYearAndMonthAndUser_Id(
                        scheduleDto.getYear(),
                        scheduleDto.getMonth(),
                        getLoginUser().getId());

        if (findSchedule.isEmpty()) {
            return scheduleRepository.save(Re_Schedule.create(scheduleDto, getLoginUser()));
        }
        return findSchedule.get();
    }

    @Transactional
    public Re_DaySchedule getDaySchedule(Re_DayScheduleDto dayScheduleDto, Long scheduleId) {
        Re_Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
        Re_DaySchedule daySchedule = dayScheduleService.getOrCreateDaySchedule(dayScheduleDto, scheduleId);

        daySchedule.addSchedule(schedule);
        return daySchedule;
    }

    /**
     *  private method
     * */
    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
