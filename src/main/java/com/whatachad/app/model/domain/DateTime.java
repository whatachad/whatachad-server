package com.whatachad.app.model.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DateTime {
    private Integer date;
    private Integer hour;
    private Integer minute;

    protected void changeDateTime(Integer hour, Integer minute) {
        this.hour = hour;
        this.minute = minute;
    }
}
