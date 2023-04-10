package com.whatachad.app.service;

import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
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

    @Transactional
    public Schedule createSchedule(ScheduleDto scheduleDto) {
        return scheduleRepository.save(Schedule.create(getLoginUser(), scheduleDto));
    }

    @Transactional
    public boolean isExistSchedule(Integer year, Integer month) {
        return scheduleRepository.existByYMonth(year, month);
    }

    @Transactional
    public Schedule findSchedule(Integer year, Integer month) {
        return scheduleRepository.findByYMonth(year, month);
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
