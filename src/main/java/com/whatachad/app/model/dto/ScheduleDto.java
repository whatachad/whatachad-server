package com.whatachad.app.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleDto {

    private Long id;
    private Integer year;
    private Integer month;
    private Integer budget;

}
