package com.whatachad.app.model.response;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayworkResponseDto {
    private Long id;
    private String title;
    private DayworkPriority priority;
    private Workcheck status;
    private LocalDate dayworkDate;
}
