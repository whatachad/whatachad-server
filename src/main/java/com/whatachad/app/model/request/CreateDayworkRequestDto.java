package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateDayworkRequestDto {

    @NotNull
    private String title;
    @NotNull
    private DayworkPriority priority;

    private Integer date;

    private Integer hour;

    private Integer minute;
}
