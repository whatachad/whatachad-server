package com.whatachad.app.service;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
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

    @Transactional
    public Daywork createDaywork(DayworkDto dayworkDto, ScheduleDto scheduleDto) {
        Schedule schedule = getSchedule(scheduleDto);
        return dayworkRepository.save(Daywork.create(schedule,dayworkDto));
    }

    private Schedule getSchedule(ScheduleDto scheduleDto) {
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
