package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Re_Schedule {

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
    List<Re_DaySchedule> days = new ArrayList<>();
}