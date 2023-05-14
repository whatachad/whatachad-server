package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.whatachad.app.util.EntityUtil.setEntity;

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
    @OrderBy("day asc")
    List<DaySchedule> daySchedules;

    public static Schedule create(ScheduleDto dto, User user) {
        return Schedule.builder()
                .user(user)
                .year(dto.getYear())
                .month(dto.getMonth())
                .budget(Objects.requireNonNullElse(dto.getBudget(), 0))
                .daySchedules(new ArrayList<>())
                .build();
    }

    public void addDaySchedule(DaySchedule daySchedule) {
        this.daySchedules.add(daySchedule);
        setEntity("schedule", daySchedule, this);
    }
}