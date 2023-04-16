package com.whatachad.app.controller;

import com.whatachad.app.api.Re_ScheduleCrudApi;
import com.whatachad.app.model.domain.Re_DaySchedule;
import com.whatachad.app.model.domain.Re_Daywork;
import com.whatachad.app.model.domain.Re_Schedule;
import com.whatachad.app.model.dto.Re_DayScheduleDto;
import com.whatachad.app.model.dto.Re_DayworkDto;
import com.whatachad.app.model.dto.Re_ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.Re_CreateDayworkRequestDto;
import com.whatachad.app.model.request.Re_UpdateDayworkRequestDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.Re_CreateDayworkResponseDto;
import com.whatachad.app.model.response.Re_UpdateDayworkResponseDto;
import com.whatachad.app.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class Re_ScheduleCrudController implements Re_ScheduleCrudApi {

    private final Re_ScheduleMapperService scheduleMapper;
    private final Re_DayScheduleMapperService dayScheduleMapper;
    private final Re_DayworkMapperService dayworkMapper;
    private final Re_ScheduleService scheduleService;
    private final Re_DayScheduleService dayScheduleService;
    private final Re_DayworkService dayworkService;

    /**
     * Daywork 관련
     * */
    @Override
    public ResponseEntity<Re_CreateDayworkResponseDto> registerDaywork(Re_CreateDayworkRequestDto requestDto, String yearAndMonth, Integer date) {
        Re_ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        Re_DayScheduleDto dayScheduleDto = dayScheduleMapper.toDayScheduleDto(date);
        Re_DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);

        Re_Schedule schedule = scheduleService.getOrCreateSchedule(scheduleDto);
        Re_DaySchedule daySchedule = scheduleService.getDaySchedule(dayScheduleDto, schedule.getId());
        Re_Daywork daywork = dayScheduleService.createDaywork(dayworkDto, daySchedule.getId());

        return ResponseEntity.ok(dayworkMapper.toCreateResponseDto(daywork));
    }

    @Override
    public ResponseEntity<Re_UpdateDayworkResponseDto> editDaywork(Re_UpdateDayworkRequestDto requestDto, Long dayworkId) {
        Re_DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);
        Re_Daywork daywork = dayworkService.updateDaywork(dayworkDto, dayworkId);

        return ResponseEntity.ok(dayworkMapper.toUpdateResponseDto(daywork));
    }

    @Override
    public void deleteDaywork(Long dayworkId) {
        dayworkService.deleteDaywork(dayworkId);
    }
}
