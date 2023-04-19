package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateDayworkRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private DayworkPriority priority;

    private Integer hour;

    private Integer minute;
}
