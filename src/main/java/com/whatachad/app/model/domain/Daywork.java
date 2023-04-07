package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Daywork extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "DAYWORK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private DayworkPriority priority;

    @Enumerated(EnumType.STRING)
    private Workcheck status;

    int time;
    int date;
}
