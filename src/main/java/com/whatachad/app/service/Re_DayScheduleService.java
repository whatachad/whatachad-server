package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Re_DaySchedule;
import com.whatachad.app.model.domain.Re_Daywork;
import com.whatachad.app.model.dto.Re_DayScheduleDto;
import com.whatachad.app.model.dto.Re_DayworkDto;
import com.whatachad.app.repository.Re_DayScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Re_DayScheduleService {

    private final Re_DayScheduleRepository dayScheduleRepository;
    private final Re_DayworkService dayworkService;

    @Transactional
    public Re_DaySchedule getOrCreateDaySchedule(Re_DayScheduleDto dayScheduleDto, Long scheduleId) {
        Optional<Re_DaySchedule> findDaySchedule = dayScheduleRepository.findBydateAndSchedule_Id(dayScheduleDto.getDate(), scheduleId);

        if (findDaySchedule.isEmpty()) {
            return dayScheduleRepository.save(Re_DaySchedule.create(dayScheduleDto));
        }
        return findDaySchedule.get();
    }

    @Transactional
    public Re_Daywork createDaywork(Re_DayworkDto dayworkDto, Long dayScheduleId) {
        Re_DaySchedule daySchedule = dayScheduleRepository.findById(dayScheduleId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daySchedule"));

        Re_Daywork daywork = dayworkService.createDaywork(dayworkDto);
        daywork.addDaySchedule(daySchedule);
        return daywork;
    }
}
