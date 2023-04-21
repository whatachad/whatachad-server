package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleCrudApi;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
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

    /**
     * Schedule 관련
     * */
    @Override
    public ResponseEntity<List<DayworksResponseDto>> getSchedule(String yearAndMonth) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        List<List<Daywork>> dayworksOnSchedule = scheduleService.findDayworksOnSchedule(scheduleDto);

        List<DayworksResponseDto> dayworksDto = new ArrayList<>();
        dayworksOnSchedule.stream().forEach(day ->{
            dayworksDto.add(new DayworksResponseDto());
            dayworksDto.get(dayworksDto.size() - 1).setDate(day.get(0).getDayworkDate());
            dayworksDto.get(dayworksDto.size() - 1).setDayworks(
                    day.stream().map(dayworkMapper::toDayworkResposneDto)
                    .toList());
        });

        return ResponseEntity.ok().body(dayworksDto);
    }
}
