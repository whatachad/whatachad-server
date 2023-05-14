package com.whatachad.app.model.mapper;

import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
import com.whatachad.app.model.vo.AccountDayworkByDay;
import com.whatachad.app.model.vo.DayworkByDay;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Comparator;
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

    public List<DayworksResponseDto> toDayworksResponseDto(List<DayworkByDay> dayworksByDay) {
        return dayworksByDay.stream()
                .map(dayworks -> new DayworksResponseDto(
                        dayworks.getDate(),
                        dayworks.getDayworks().stream()
                                .sorted(Comparator.comparingInt(o -> o.getPriority().getValue()))
                                .limit(3)
                                .map(dayworkConverter::toDayworkResponseDto).toList()))
                .toList();
    }

    public Slice<RecentScheduleResponseDto> toRecentScheduleResponseDto(Slice<AccountDayworkByDay> allOnSchedule) {
        return allOnSchedule.map(day -> {
            RecentScheduleResponseDto recentScheduleResponseDto = new RecentScheduleResponseDto();
            recentScheduleResponseDto.setAccounts(
                    day.getAccounts().stream()
                            .sorted(Comparator.comparingInt(o -> o.getType().getValue()))
                            .map(accountConverter::toAccountResponseDto).toList()
            );

            recentScheduleResponseDto.setDayworks(
                    day.getDayworks().stream()
                            .sorted(Comparator.comparingInt(o -> o.getPriority().getValue()))
                            .map(dayworkConverter::toDayworkResponseDto).toList()
            );

            recentScheduleResponseDto.setDate(day.getDate());
            return recentScheduleResponseDto;
        });
    }


}
