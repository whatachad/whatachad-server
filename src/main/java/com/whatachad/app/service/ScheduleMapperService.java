package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
import com.whatachad.app.model.response.ScheduleResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Service
public class ScheduleMapperService {

    private final DayworkMapperService dayworkMapper;
    private final AccountMapperService accountMapper;

    public ScheduleDto toScheduleDto(String yearAndMonth) {
        String year = yearAndMonth.substring(0, 4);
        String month = yearAndMonth.substring(4);

        return ScheduleDto.builder()
                .year(Integer.valueOf(year))
                .month(Integer.valueOf(month))
                .build();
    }

    public ScheduleResponseDto toScheduleResponseDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .year(schedule.getYear())
                .month(schedule.getMonth())
                .budget(schedule.getBudget())
                .build();
    }

    public List<DayworksResponseDto> toDayworksResponseDto(List<List<Daywork>> dayworksByDay) {
        List<DayworksResponseDto> dayworksDto = new ArrayList<>();
        dayworksByDay.stream().forEach(day ->{
            dayworksDto.add(new DayworksResponseDto());
            dayworksDto.get(dayworksDto.size() - 1).setDate(day.get(0).getDayworkDate());
            dayworksDto.get(dayworksDto.size() - 1).setDayworks(
                    day.stream().map(dayworkMapper::toDayworkResposneDto)
                            .toList());
        });
        return dayworksDto;
    }

    public Slice<RecentScheduleResponseDto> toRecentScheduleResponseDto(Slice<List<List<Object>>> allOnSchedule) {
         return allOnSchedule.map(day -> {
            RecentScheduleResponseDto scheduleRecentDto = new RecentScheduleResponseDto();
            scheduleRecentDto.setAccounts(
                    day.get(0).stream()
                            .map(object -> (Account) object)
                            .map(accountMapper::toAccountResponseDto).toList()
            );

            scheduleRecentDto.setDayworks(
                    day.get(1).stream()
                            .map(object -> (Daywork) object)
                            .map(dayworkMapper::toDayworkResposneDto).toList()
            );

            scheduleRecentDto.setDate(scheduleRecentDto.getAccounts().isEmpty()?
                    scheduleRecentDto.getDayworks().get(0).getDate() : scheduleRecentDto.getAccounts().get(0).getDate());
            return scheduleRecentDto;
        });
    }


}
