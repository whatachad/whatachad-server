package com.whatachad.app.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class RecentScheduleResponseDto {
    LocalDate date;
    List<AccountResponseDto> accounts;
    List<DayworkResponseDto> dayworks;
}
