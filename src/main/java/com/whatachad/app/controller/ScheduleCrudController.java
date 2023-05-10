package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleCrudApi;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.mapper.AccountConverter;
import com.whatachad.app.model.mapper.DayworkConverter;
import com.whatachad.app.model.mapper.ScheduleConverter;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.AccountResponseDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
import com.whatachad.app.model.vo.AccountDayworkByDay;
import com.whatachad.app.model.vo.DayworkByDay;
import com.whatachad.app.service.AccountService;
import com.whatachad.app.service.DayworkService;
import com.whatachad.app.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ScheduleCrudController implements ScheduleCrudApi {

    private final ScheduleConverter scheduleConverter;
    private final AccountConverter accountConverter;
    private final DayworkConverter dayworkConverter;
    private final ScheduleService scheduleService;
    private final AccountService accountService;
    private final DayworkService dayworkService;

    /**
     * Daywork 관련
     */
    @Override
    public ResponseEntity<DayworkResponseDto> registerDaywork(CreateDayworkRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleConverter.toScheduleDto(yearAndMonth);
        DayworkDto dayworkDto = dayworkConverter.toDayworkDto(requestDto);
        Daywork daywork = scheduleService.createDayworkOnSchedule(date, dayworkDto, scheduleDto);
        return ResponseEntity.ok(dayworkConverter.toDayworkResponseDto(daywork));
    }

    @Override
    public ResponseEntity<DayworkResponseDto> editDaywork(UpdateDayworkRequestDto requestDto, Long dayworkId) {
        DayworkDto dayworkDto = dayworkConverter.toDayworkDto(requestDto);
        dayworkService.updateDaywork(dayworkDto, dayworkId);
        Daywork daywork = dayworkService.findDayworkById(dayworkId);
        return ResponseEntity.ok(dayworkConverter.toDayworkResponseDto(daywork));
    }

    @Override
    public void deleteDaywork(Long dayworkId) {
        dayworkService.deleteDaywork(dayworkId);
    }

    /**
     * Account 관련
     */
    @Override
    public ResponseEntity<AccountResponseDto> registerAccount(CreateAccountRequestDto requestDto, String yearAndMonth, Integer date) {
        ScheduleDto scheduleDto = scheduleConverter.toScheduleDto(yearAndMonth);
        AccountDto accountDto = accountConverter.toAccountDto(requestDto);
        Account account = scheduleService.createAccountOnSchedule(date, accountDto, scheduleDto);
        return ResponseEntity.ok(accountConverter.toAccountResponseDto(account));
    }

    @Override
    public ResponseEntity<AccountResponseDto> editAccount(UpdateAccountRequestDto requestDto, Long accountId) {
        AccountDto accountDto = accountConverter.toAccountDto(requestDto);
        accountService.updateAccount(accountDto, accountId);
        Account account = accountService.findAccountById(accountId);
        return ResponseEntity.ok(accountConverter.toAccountResponseDto(account));
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountService.findAccountById(accountId);
        accountService.deleteAccount(account.getId());
    }

    /**
     * Schedule 관련
     */
    @Override
    public ResponseEntity<List<DayworksResponseDto>> getSchedule(String yearAndMonth) {
        ScheduleDto scheduleDto = scheduleConverter.toScheduleDto(yearAndMonth);
        List<DayworkByDay> dayworksOnSchedule = scheduleService.findDayworksOnSchedule(scheduleDto);
        List<DayworksResponseDto> dayworksDto = scheduleConverter.toDayworksResponseDto(dayworksOnSchedule);
        return ResponseEntity.ok().body(dayworksDto);
    }

    @Override
    public ResponseEntity<Slice<RecentScheduleResponseDto>> getRecentSchedule(String yearAndMonth, Integer page) {
        ScheduleDto scheduleDto = scheduleConverter.toScheduleDto(yearAndMonth);
        Slice<AccountDayworkByDay> allOnSchedule = scheduleService.findAllOnSchedule(scheduleDto, page);
        Slice<RecentScheduleResponseDto> scheduleRecentResponseDto = scheduleConverter.toRecentScheduleResponseDto(allOnSchedule);
        return ResponseEntity.ok(scheduleRecentResponseDto);
    }

}
