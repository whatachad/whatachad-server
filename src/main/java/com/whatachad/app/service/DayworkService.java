package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
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
    public Daywork findDayworkById(Long id) {
        return dayworkRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));
    }

    @Transactional(readOnly = true)
    public boolean isDayworkBySchedule(Long schedule_id) {
        Schedule schedule = scheduleService.findSchedule(schedule_id);
        return dayworkRepository.existBySchedule(schedule);
    }

    @Transactional
    public void deleteDaywork(Long daywork_id) {
        dayworkRepository.deleteById(daywork_id);
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
