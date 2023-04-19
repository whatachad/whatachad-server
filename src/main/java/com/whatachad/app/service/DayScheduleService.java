package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.repository.DayScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DayScheduleService {

    private final DayScheduleRepository dayScheduleRepository;
    private final DayworkService dayworkService;

    @Transactional
    public Daywork createDayworkOnDay(DayworkDto dayworkDto, Long dayScheduleId) {
        DaySchedule daySchedule =  dayScheduleRepository.findById(dayScheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));

        Daywork daywork = dayworkService.createDaywork(dayworkDto);
        daywork.addDaySchedule(daySchedule);
        return daywork;
    }

    @Transactional(readOnly = true)
    public List<DaySchedule> findDaySchedulesOnSchedule(Long scheduleId) {
        return dayScheduleRepository.findBySchedule_IdOrderByDateAsc(scheduleId);
    }

    @Transactional(readOnly = true)
    public DaySchedule findDaySchedule(Integer date, Long scheduleId) {
        return dayScheduleRepository.findBydateAndSchedule_Id(date, scheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));
    }

    @Transactional
    public DaySchedule getOrCreateDaySchedule(Integer date, Long scheduleId) {
        Optional<DaySchedule> findDaySchedule = dayScheduleRepository.findBydateAndSchedule_Id(date, scheduleId);

        if (findDaySchedule.isEmpty()) {
            return dayScheduleRepository.save(DaySchedule.createByDate(date));
        }
        return findDaySchedule.get();
    }
}
