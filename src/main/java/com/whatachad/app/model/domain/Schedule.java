package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.util.EntityUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.whatachad.app.util.EntityUtils.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule {

    @Id @GeneratedValue
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private  User user;

    private Integer year;

    private Integer month;

    private Integer budget;

    @OneToMany(mappedBy = "schedule")
    List<DaySchedule> daySchedules = new ArrayList<>();

    public static Schedule create(ScheduleDto dto, User user) {
        return Schedule.builder()
                .user(user)
                .year(dto.getYear())
                .month(dto.getMonth())
                .budget(dto.getBudget())
                .build();
    }

    public void addDaySchedule(DaySchedule daySchedule) {
        this.daySchedules.add(daySchedule);
        setEntity("schedule", daySchedule, this);
    }

}