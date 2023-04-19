package com.whatachad.app.service;

import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.dto.DayScheduleDto;
import com.whatachad.app.model.response.DayScheduleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DayScheduleMapperService {

    public DayScheduleDto toDayScheduleDto(Integer date) {
        return DayScheduleDto.builder()
                .date(date)
                .build();
    }

    public DayScheduleResponseDto toDayScheduleResponseDto(DaySchedule daySchedule) {
        return DayScheduleResponseDto.builder()
                .id(daySchedule.getId())
                .date(daySchedule.getDate())
                .build();
    }
}
