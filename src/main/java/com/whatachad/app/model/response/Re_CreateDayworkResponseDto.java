package com.whatachad.app.model.response;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
public class Re_CreateDayworkResponseDto {
    private Long id;
    private String title;
    private DayworkPriority priority;
    private Workcheck status;
    private Integer hour;
    private Integer minute;
}
