package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DayworkConverter {

    private final DayworkMapper dayworkMapper;

    public DayworkDto toDayworkDto(String yearAndMonth, Integer day, CreateDayworkRequestDto dto) {
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(dto);
        int year = Integer.parseInt(yearAndMonth.substring(0, 4));
        int month = Integer.parseInt(yearAndMonth.substring(4));
        dayworkDto.setDayworkDate(LocalDate.of(year, month, day));
        return dayworkDto;
    }

    public DayworkDto toDayworkDto(UpdateDayworkRequestDto dto) {
        return dayworkMapper.toDayworkDto(dto);
    }
    public DayworkResponseDto toDayworkResponseDto(Daywork daywork) {
        return dayworkMapper.toDayworkResponseDto(daywork);
    }
}
