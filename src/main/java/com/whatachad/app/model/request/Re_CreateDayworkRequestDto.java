package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Re_CreateDayworkRequestDto {

    @NotNull
    private String title;

    @NotNull
    private DayworkPriority priority;

    private Integer hour;

    private Integer minute;
}
