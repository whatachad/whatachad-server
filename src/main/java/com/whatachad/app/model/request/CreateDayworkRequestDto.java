package com.whatachad.app.model.request;

import com.whatachad.app.type.DayworkPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateDayworkRequestDto {

    @NotBlank(message = "{NotBlank.daywork.title}")
    @Length(max = 30, message = "{Length.daywork.title}")
    private String title;

    @NotNull(message = "{NotNull.daywork.priority}")
    private DayworkPriority priority;
}
