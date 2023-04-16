package com.whatachad.app.model.dto;

import com.whatachad.app.type.Workcheck;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Re_DayScheduleDto {

    private Long id;
    private Integer date;
    private Workcheck totalDayworkStatus;
}
