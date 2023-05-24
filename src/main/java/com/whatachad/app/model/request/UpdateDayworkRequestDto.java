package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateDayworkRequestDto {

    @NotBlank(message = "{NotBlank.daywork.title}")
    @Length(max = 30, message = "{Length.daywork.title}")
    private String title;

    @NotNull(message = "{NotNull.daywork.priority}")
    private DayworkPriority priority;

    @NotNull(message = "{NotNull.workcheck.status}")
    private Workcheck status;

}
