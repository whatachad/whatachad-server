package com.whatachad.app.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class DayScheduleAndDayworksResponseDto {
    DayScheduleResponseDto dayschedule;
    List<DayworkResponseDto> dayworks;
}
