package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.repository.DayworkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
        findDaywork.update(dayworkDto);
    }

    @Transactional(readOnly = true)
    public Daywork findDayworkById(Long dayworkId) {
        return dayworkRepository.findById(dayworkId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Daywork"));
    }

    @Transactional
    public void deleteDaywork(Long dayworkId) {
        dayworkRepository.deleteById(dayworkId);
    }

    @Transactional
    public List<Daywork> findDayworks(Long dayScheduleId) {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return dayworkRepository.findByDaySchedule_Id(dayScheduleId, pageRequest);
    }
}
