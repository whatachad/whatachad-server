package com.whatachad.app.model.response;

import com.whatachad.app.type.Workcheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DayScheduleResponseDto {
    private Long id;
    private Integer date;
}
