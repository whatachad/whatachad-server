package com.whatachad.app.model.response;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class CreateDayworkResponseDto {

    private String title;
    private DayworkPriority priority;
    private Workcheck status;
}
