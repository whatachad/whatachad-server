package com.whatachad.app.controller;

import com.whatachad.app.api.DayworkApi;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.service.DayworkMapperService;
import com.whatachad.app.service.DayworkService;
import com.whatachad.app.service.ScheduleMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DayworkController implements DayworkApi {

    private final DayworkService dayworkService;
    private final ScheduleMapperService scheduleMapper;
    private final DayworkMapperService dayworkMapper;

    @Override
    public ResponseEntity<CreateDayworkResponseDto> registerDaywork(CreateDayworkRequestDto requestDto, String ym, Integer date) {
        log.info("registerDaywork 진입");
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(ym);
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto, date);

        Daywork daywork = dayworkService.createDaywork(dayworkDto, scheduleDto);
        return ResponseEntity.ok(dayworkMapper.toCreateResponseDto(daywork));
    }

}
