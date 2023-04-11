package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.repository.DayworkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DayworkService {

    private final ScheduleService scheduleService;
    private final DayworkRepository dayworkRepository;
    private final UserService userService;

    @Transactional
    public Daywork createDaywork(DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = getOrCreateSchedule(scheduleDto);
        return dayworkRepository.save(Daywork.create(schedule,dayworkDto));
    }

    @Transactional
    public void updateDaywork(DayworkDto dayworkDto, Long daywork_id) {
        Daywork findDaywork = dayworkRepository.findById(daywork_id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));

        findDaywork.updateDaywork(dayworkDto);
    }

    @Transactional(readOnly = true)
    public Daywork findDaywork(Long id) {
        return dayworkRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));
    }

    private Schedule getOrCreateSchedule(ScheduleDto scheduleDto) {
        boolean existSchedule = scheduleService.isExistSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        Schedule schedule = null;

        if(existSchedule){
            schedule = scheduleService.findSchedule(scheduleDto.getYear(), scheduleDto.getMonth());
        }else {
            schedule = scheduleService.createSchedule(scheduleDto);
        }
        return schedule;
    }
}
