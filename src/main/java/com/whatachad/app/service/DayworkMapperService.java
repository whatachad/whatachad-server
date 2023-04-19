package com.whatachad.app.service;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DayworkMapperService {
    public DayworkDto toDayworkDto(CreateDayworkRequestDto requestDto) {
        return DayworkDto.builder()
                .title(requestDto.getTitle())
                .priority(requestDto.getPriority())
                .hour(requestDto.getHour())
                .minute(requestDto.getMinute())
                .build();
    }

    public DayworkResponseDto toCreateResponseDto(Daywork daywork) {
        return DayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .hour(daywork.getHour())
                .minute(daywork.getMinute())
                .build();
    }

    public DayworkDto toDayworkDto(UpdateDayworkRequestDto requestDto) {
        return DayworkDto.builder()
                .title(requestDto.getTitle())
                .priority(requestDto.getPriority())
                .status(requestDto.getStatus())
                .hour(requestDto.getHour())
                .minute(requestDto.getMinute())
                .build();
    }

    public UpdateDayworkResponseDto toUpdateResponseDto(Daywork daywork) {
        return UpdateDayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .hour(daywork.getHour())
                .minute(daywork.getMinute())
                .build();
    }

    public DayworkResponseDto toDayworkResposneDto(Daywork daywork) {
        return DayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .hour(daywork.getHour())
                .minute(daywork.getMinute())
                .build();
    }
}
