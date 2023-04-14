package com.whatachad.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer budget;

}
