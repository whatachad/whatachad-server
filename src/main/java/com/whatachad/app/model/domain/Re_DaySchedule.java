package com.whatachad.app.model.domain;

import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Re_DaySchedule {

    @Id @GeneratedValue
    @Column(name = "DAYSCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Re_Schedule schedule;

    private Integer date;

    @Enumerated(EnumType.STRING)
    private Workcheck totalDayworkStatus;

}