package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.vo.AccountDayworkByDay;
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

    public Slice<RecentScheduleResponseDto> toRecentScheduleResponseDto(Slice<AccountDayworkByDay> allOnSchedule) {
        return allOnSchedule.map(day -> {
            RecentScheduleResponseDto recentScheduleResponseDto = new RecentScheduleResponseDto();
            recentScheduleResponseDto.setAccounts(
                    day.getAccounts().stream()
                            .map(accountConverter::toAccountResponseDto).toList()
            );

            recentScheduleResponseDto.setDayworks(
                    day.getDayworks().stream()
                            .map(dayworkConverter::toDayworkResponseDto).toList()
            );

            recentScheduleResponseDto.setDate(day.getDate());
            return recentScheduleResponseDto;
        });
    }


}
