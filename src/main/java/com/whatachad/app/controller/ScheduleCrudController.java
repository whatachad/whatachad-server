package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleCrudApi;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.CreateAccountResponseDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.UpdateAccountResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
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
public class ScheduleCrudController implements ScheduleCrudApi {

    private final DayworkService dayworkService;
    private final ScheduleService scheduleService;
    private final ScheduleMapperService scheduleMapper;
    private final DayworkMapperService dayworkMapper;
    private final AccountMapperService accountMapper;
    private final AccountService accountService;
    /**
     *  Daywork 관련
     * */
    @Override
    public ResponseEntity<CreateDayworkResponseDto> registerDaywork(CreateDayworkRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto, date);

        Daywork daywork =scheduleService.createDayworkOnSchedule(dayworkDto, scheduleDto);
        return ResponseEntity.ok(dayworkMapper.toCreateResponseDto(daywork));
    }

    @Override
    public ResponseEntity<UpdateDayworkResponseDto> editDaywork(UpdateDayworkRequestDto requestDto, Long dayworkId) {
        // todo : 스케쥴 관련(Year Month date)도 변경할 수 있게?
        DayworkDto dayworkDto = dayworkMapper.toDayworkDto(requestDto);
        dayworkService.updateDaywork(dayworkDto, dayworkId);
        Daywork daywork = dayworkService.findDayworkById(dayworkId);
        return ResponseEntity.ok(dayworkMapper.toUpdateResponseDto(daywork));
    }

    @Override
    public void deleteDaywork(Long dayworkId) {
        Daywork daywork = dayworkService.findDayworkById(dayworkId);
        Schedule schedule = daywork.getSchedule();
        dayworkService.deleteDaywork(dayworkId);
    }

    /**
     *  Account 관련
     * */
    @Override
    public ResponseEntity<CreateAccountResponseDto> registerAccount(CreateAccountRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(yearAndMonth);
        AccountDto accountDto = accountMapper.toAccountDto(requestDto, date);

        Account account = scheduleService.createAccountOnSchedule(accountDto, scheduleDto);
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
        Schedule schedule = account.getSchedule();
        accountService.deleteAccount(accountId);
    }
}
