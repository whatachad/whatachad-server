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

    public DayworkDto toDayworkDto(CreateDayworkRequestDto dto, Integer date) {
        return DayworkDto.builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .build();
    }

    public DayworkDto toDayworkDto(UpdateDayworkRequestDto dto){
        return DayworkDto.builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .status(dto.getStatus())
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
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .build();
    }

    public DayworkResponseDto toDayworkResponseDto(Daywork daywork) {
        return DayworkResponseDto.builder()
                .id(daywork.getId())
                .title(daywork.getTitle())
                .priority(daywork.getPriority())
                .status(daywork.getStatus())
                .build();
    }

}
