package com.whatachad.app.service;

import com.whatachad.app.model.dto.ScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleMapperService {

    public ScheduleDto toScheduleDto(String yearAndMonth){
        String year = yearAndMonth.substring(0, 4);
        String month = yearAndMonth.substring(4);

        return ScheduleDto.builder()
                .year(Integer.valueOf(year))
                .month(Integer.valueOf(month))
                .build();
    }
}
