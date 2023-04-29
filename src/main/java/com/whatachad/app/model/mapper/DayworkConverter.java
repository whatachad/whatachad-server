package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DayworkConverter {

    private final DayworkMapper dayworkMapper;

    public DayworkDto toDayworkDto(CreateDayworkRequestDto dto) {
        return dayworkMapper.toDayworkDto(dto);
    }

    public DayworkDto toDayworkDto(UpdateDayworkRequestDto dto) {
        return dayworkMapper.toDayworkDto(dto);
    }
    public DayworkResponseDto toDayworkResponseDto(Daywork daywork) {
        return dayworkMapper.toDayworkResponseDto(daywork);
    }
}
