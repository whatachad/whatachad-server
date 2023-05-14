package com.whatachad.app.model.vo;

import com.whatachad.app.model.domain.Daywork;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DayworkByDay {

    private LocalDate date;
    private List<Daywork> dayworks;

    public DayworkByDay(LocalDate date, List<Daywork> dayworks) {
        this.date = date;
        this.dayworks = new ArrayList<>(dayworks);
    }
}
