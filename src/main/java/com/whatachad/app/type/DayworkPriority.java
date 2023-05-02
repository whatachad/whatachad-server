package com.whatachad.app.type;

import lombok.Getter;

@Getter
public enum DayworkPriority {

    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int value;

    DayworkPriority(int value) {
        this.value = value;
    }

}
