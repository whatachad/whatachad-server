package com.whatachad.app.model.dto;

import lombok.Getter;

@Getter
public class DemoDto {
    private String name;

    public DemoDto(String name) {
        this.name = name;
    }
}
