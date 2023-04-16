package com.whatachad.app.service;

import com.whatachad.app.model.dto.Re_DayScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Re_DayScheduleMapperService {

    public Re_DayScheduleDto toDayScheduleDto(Integer date) {
        return Re_DayScheduleDto.builder()
                .date(date)
                .build();
    }
}
