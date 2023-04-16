package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Re_Daywork;
import com.whatachad.app.model.dto.Re_DayworkDto;
import com.whatachad.app.repository.Re_DayworkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Re_DayworkService {

    private final Re_DayworkRepository dayworkRepository;

    @Transactional
    public Re_Daywork createDaywork(Re_DayworkDto dayworkDto) {
        return dayworkRepository.save(Re_Daywork.create(dayworkDto));
    }

    @Transactional
    public Re_Daywork updateDaywork(Re_DayworkDto dayworkDto, Long dayworkId) {
        Re_Daywork findDaywork = dayworkRepository.findById(dayworkId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "daywork"));
        findDaywork.updateDaywork(dayworkDto);

        return findDaywork;
    }

    @Transactional
    public void deleteDaywork(Long dayworkId) {
        dayworkRepository.deleteById(dayworkId);
    }
}
