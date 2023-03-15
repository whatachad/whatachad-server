package com.whatachad.app.model.dto;

import lombok.Getter;

@Getter
public class ScheduleDto {

    private String name;

    public ScheduleDto(String name) {
        this.name = name;
    }
}
