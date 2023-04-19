package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleCrudApi;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayScheduleDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.*;
import com.whatachad.app.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ScheduleCrudController implements ScheduleCrudApi {

    private final ScheduleMapperService scheduleMapper;
    private final AccountMapperService accountMapper;
    private final DayScheduleMapperService dayScheduleMapper;
    private final DayworkMapperService dayworkMapper;
    private final ScheduleService scheduleService;
    private final DayScheduleService dayScheduleService;
    private final AccountService accountService;
    private final DayworkService dayworkService;

    /**
     * Daywork 관련
     * */
    @Override
    public ResponseEntity<CreateDayworkResponseDto> registerDaywork(CreateDayworkRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);
        Daywork daywork = scheduleService.createDayworkOnSchedule(date, dayworkDto, scheduleDto);
        return ResponseEntity.ok(dayworkMapper.toCreateResponseDto(daywork));
    }

    @Override
    public ResponseEntity<UpdateDayworkResponseDto> editDaywork(UpdateDayworkRequestDto requestDto, Long dayworkId) {
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);
        dayworkService.updateDaywork(dayworkDto, dayworkId);
        Daywork daywork = dayworkService.findDayworkById(dayworkId);
        return ResponseEntity.ok(dayworkMapper.toUpdateResponseDto(daywork));
    }

    @Override
    public void deleteDaywork(Long dayworkId) {
        dayworkService.deleteDaywork(dayworkId);
    }

    /**
     * Account 관련
     */
    @Override
    public ResponseEntity<CreateAccountResponseDto> registerAccount(CreateAccountRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        AccountDto accountDto = accountMapper.toAccountDto(requestDto);
        Account account = scheduleService.createAccountOnSchedule(date, accountDto, scheduleDto);
        return ResponseEntity.ok(accountMapper.toCreateAccountResponseDto(account));
    }

    @Override
    public ResponseEntity<UpdateAccountResponseDto> editAccount(UpdateAccountRequestDto requestDto, Long accountId) {
        AccountDto accountDto = accountMapper.toAccountDto(requestDto);
        accountService.updateAccount(accountDto, accountId);
        Account account = accountService.findAccountById(accountId);
        return ResponseEntity.ok(accountMapper.toUpdateResponseDto(account));
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountService.findAccountById(accountId);
        accountService.deleteAccount(account.getId());
    }

    @Override
    public ResponseEntity<Map<String, Object>> getSchedule(String yearAndMonth) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        Schedule schedule = scheduleService.findSchedule(scheduleDto);
        ScheduleResponseDto scheduleResponseDto = scheduleMapper.toScheduleResponseDto(schedule);

        List<DaySchedule> daySchedules = dayScheduleService.findDaySchedulesOnSchedule(schedule.getId());

        List<DayScheduleAndDayworksResponseDto> dayScheduleAndDayworksResponse = new ArrayList<>();
        daySchedules.forEach(daySchedule -> {
            DayScheduleAndDayworksResponseDto bindingDayscheduleAndDayworks = new DayScheduleAndDayworksResponseDto();
            bindingDayscheduleAndDayworks.setDayschedule(dayScheduleMapper.toDayScheduleResponseDto(daySchedule));

            List<Daywork> dayworks = dayworkService.findDayworks(daySchedule.getId());
            List<DayworkResponseDto> re_dayworkResponseDtos = dayworks.stream()
                    .map(daywork -> dayworkMapper.toDayworkResposneDto(daywork))
                    .toList();
            bindingDayscheduleAndDayworks.setDayworks(re_dayworkResponseDtos);

            dayScheduleAndDayworksResponse.add(bindingDayscheduleAndDayworks);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("schedule", scheduleResponseDto);
        response.put("dayscheduleAndDayworks", dayScheduleAndDayworksResponse);

        return ResponseEntity.ok().body(response);
    }
}
