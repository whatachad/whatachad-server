package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleConverter {

    private final DayworkConverter dayworkConverter;
    private final AccountConverter accountConverter;

    public ScheduleDto toScheduleDto(String yearAndMonth) {
        String year = yearAndMonth.substring(0, 4);
        String month = yearAndMonth.substring(4);

        return ScheduleDto.builder()
                .year(Integer.valueOf(year))
                .month(Integer.valueOf(month))
                .build();
    }

    public List<DayworksResponseDto> toDayworksResponseDto(List<List<Daywork>> dayworksByDay) {
        List<DayworksResponseDto> dayworksDto = new ArrayList<>();
        dayworksByDay.stream().forEach(day -> {
            dayworksDto.add(new DayworksResponseDto(
                    day.get(day.size() - 1).getDayworkDate(),
                    day.stream().map(dayworkConverter::toDayworkResponseDto).toList()));
        });
        return dayworksDto;
    }

    public Slice<RecentScheduleResponseDto> toRecentScheduleResponseDto(Slice<List<List<Object>>> allOnSchedule) {
        return allOnSchedule.map(day -> {
            RecentScheduleResponseDto scheduleRecentDto = new RecentScheduleResponseDto();
            scheduleRecentDto.setAccounts(
                    day.get(0).stream()
                            .map(object -> (Account) object)
                            .map(accountConverter::toAccountResponseDto).toList()
            );

            scheduleRecentDto.setDayworks(
                    day.get(1).stream()
                            .map(object -> (Daywork) object)
                            .map(dayworkConverter::toDayworkResponseDto).toList()
            );

            scheduleRecentDto.setDate(scheduleRecentDto.getAccounts().isEmpty()?
                    scheduleRecentDto.getDayworks().get(0).getDate() : scheduleRecentDto.getAccounts().get(0).getAccountDate());
            return scheduleRecentDto;
        });
    }
}
