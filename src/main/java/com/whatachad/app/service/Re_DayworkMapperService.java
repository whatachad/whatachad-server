package com.whatachad.app.service;

import com.whatachad.app.model.domain.Re_Daywork;
import com.whatachad.app.model.dto.Re_DayworkDto;
import com.whatachad.app.model.request.Re_CreateDayworkRequestDto;
import com.whatachad.app.model.response.Re_CreateDayworkResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Re_DayworkMapperService {
    public Re_DayworkDto toDayworkDto(Re_CreateDayworkRequestDto requestDto) {
        return Re_DayworkDto.builder()
                .title(requestDto.getTitle())
                .priority(requestDto.getPriority())
                .hour(requestDto.getHour())
                .minute(requestDto.getMinute())
                .build();
    }

    public Re_CreateDayworkResponseDto toCreateResponseDto(Re_Daywork daywork) {
        return Re_CreateDayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .hour(daywork.getHour())
                .minute(daywork.getMinute())
                .build();
    }
}
