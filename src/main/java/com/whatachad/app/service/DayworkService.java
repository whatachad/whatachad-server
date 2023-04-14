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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DayworkService {

    private final DayworkRepository dayworkRepository;

    @Transactional
    public Daywork createDaywork(DayworkDto dayworkDto) {
        return dayworkRepository.save(Daywork.create(dayworkDto));
    }

    @Transactional
    public void updateDaywork(DayworkDto dayworkDto, Long dayworkId) {
        Daywork findDaywork = dayworkRepository.findById(dayworkId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));

        findDaywork.updateDaywork(dayworkDto);
    }

    @Transactional(readOnly = true)
    public Daywork findDayworkById(Long dayworkId) {
        return dayworkRepository.findById(dayworkId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));
    }

    @Transactional(readOnly = true)
    public List<Daywork> findDayworkBySchedule(Long scheduleId) {
        return dayworkRepository.findAllBySchedule(scheduleId);
    }

    @Transactional
    public void deleteDaywork(Long dayworkId) {
        dayworkRepository.deleteById(dayworkId);
    }

}
