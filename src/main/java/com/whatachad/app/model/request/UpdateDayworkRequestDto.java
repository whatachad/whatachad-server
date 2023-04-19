package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateDayworkRequestDto {

    private String title;

    private DayworkPriority priority;

    private Workcheck status;

    private Integer hour;

    private Integer minute;
}
