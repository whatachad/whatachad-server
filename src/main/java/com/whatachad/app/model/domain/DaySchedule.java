package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayScheduleDto;
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
public class DaySchedule {

    @Id @GeneratedValue
    @Column(name = "DAYSCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    private Integer date;

    @Enumerated(EnumType.STRING)
    private Workcheck totalDayworkStatus;

    @OneToMany(mappedBy = "daySchedule")
    private List<Daywork> dayworks = new ArrayList<>();

    @OneToMany(mappedBy = "daySchedule")
    private List<Account> accounts = new ArrayList<>();

    public static DaySchedule create(DayScheduleDto dto) {
        return DaySchedule.builder()
                .date(dto.getDate())
                .build();
    }

    public static DaySchedule createByDate(Integer date) {
        return DaySchedule.builder()
                .date(date)
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.totalDayworkStatus = Workcheck.NOT_COMPLETE;
    }

    /* 연관관계 편의 메소드 */
    public void addSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.getDays().add(this);
    }
}