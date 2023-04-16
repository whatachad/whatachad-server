package com.whatachad.app.model.domain;

import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "daySchedule")
    private List<Re_Daywork> dayworks = new ArrayList<>();

    @OneToMany(mappedBy = "daySchedule")
    private List<Re_Account> accounts = new ArrayList<>();

    /* 연관관계 편의 메소드 */
    public void addSchedule(Re_Schedule schedule) {
        this.schedule = schedule;
        schedule.getDays().add(this);
    }
}