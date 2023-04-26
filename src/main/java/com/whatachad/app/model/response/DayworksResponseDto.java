package com.whatachad.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DayworksResponseDto {
    LocalDate date;
    List<DayworkResponseDto> dayworks;
}
