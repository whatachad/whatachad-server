package com.whatachad.app.model.domain;

import com.whatachad.app.model.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedule")
public class Schedule extends BaseTime{
    @Id
    @Column(name = "schedule_id")
    private String id;

    @Column(name = "schedule_year")
    private String year;

    @Column(name = "schedule_month")
    private String month;

    @Column(name = "schedule_day")
    private String day;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status; // 완료상태 [COMPLETE, NOT_COMPLETED]

    @Column
    private int totalExpenditure;

    @Column
    private int totalIncome;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "schedule")
    private List<DayWork> dayWorks = new ArrayList<>();
    @OneToMany(mappedBy = "schedule")
    private List<Account> accounts = new ArrayList<>();
}
