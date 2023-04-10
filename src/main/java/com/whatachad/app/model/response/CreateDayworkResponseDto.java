package com.whatachad.app.model.response;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDayworkResponseDto {

    private String title;
    private DayworkPriority priority;
    private Workcheck status;
    private DateTime dateTime;
}
