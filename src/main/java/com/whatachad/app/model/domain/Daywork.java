package com.whatachad.app.model.domain;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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

    @Embedded
    private DateTime dateTime;
}

