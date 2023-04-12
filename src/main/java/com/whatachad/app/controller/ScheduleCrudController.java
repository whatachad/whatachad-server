package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleCrudApi;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
import com.whatachad.app.service.DayworkMapperService;
import com.whatachad.app.service.DayworkService;
import com.whatachad.app.service.ScheduleMapperService;
import com.whatachad.app.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ScheduleCrudController implements ScheduleCrudApi {

    private final DayworkService dayworkService;
    private final ScheduleService scheduleService;
    private final ScheduleMapperService scheduleMapper;
    private final DayworkMapperService dayworkMapper;

    /**
     *  Daywork 관련
     * */
    @Override
    public ResponseEntity<CreateDayworkResponseDto> registerDaywork(CreateDayworkRequestDto requestDto, String ym, Integer date) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(ym);
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto, date);

        Daywork daywork =scheduleService.createDayworkOnSchedule(dayworkDto, scheduleDto);
        return ResponseEntity.ok(dayworkMapper.toCreateResponseDto(daywork));
    }

    @Override
    public ResponseEntity<UpdateDayworkResponseDto> editDaywork(UpdateDayworkRequestDto requestDto, Long daywork_id) {
        // todo : 스케쥴 관련(Year Month date)도 변경할 수 있게?
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);
        dayworkService.updateDaywork(dayworkDto, daywork_id);
        Daywork daywork = dayworkService.findDayworkById(daywork_id);
        return ResponseEntity.ok(dayworkMapper.toUpdateResponseDto(daywork));
    }

    @Override
    public void deleteDaywork(Long daywork_id) {
        Daywork daywork = dayworkService.findDayworkById(daywork_id);
        Schedule schedule = daywork.getSchedule();
        dayworkService.deleteDaywork(daywork_id);

        boolean remainDaywork = dayworkService.isDayworkBySchedule(schedule.getId());
        if(!remainDaywork){
            scheduleService.deleteSchedule(schedule.getId());
        }
    }

}
