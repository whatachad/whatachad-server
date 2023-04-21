package com.whatachad.app.service;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DayworkMapperService {
    public DayworkDto toDayworkDto(CreateDayworkRequestDto dto) {
        return DayworkDto.builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .build();
    }

    public DayworkDto toDayworkDto(UpdateDayworkRequestDto requestDto) {
        return DayworkDto.builder()
                .title(requestDto.getTitle())
                .priority(requestDto.getPriority())
                .status(requestDto.getStatus())
                .build();
    }

    public CreateDayworkResponseDto toCreateResponseDto(Daywork daywork) {
        return CreateDayworkResponseDto.builder()
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .build();
    }

    public UpdateDayworkResponseDto toUpdateResponseDto(Daywork daywork) {
        return UpdateDayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .build();
    }

    public DayworkResponseDto toDayworkResposneDto(Daywork daywork) {
        return DayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .build();
    }
}
