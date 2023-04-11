package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayworkDto;
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

    public static Daywork create(Schedule schedule, DayworkDto dto) {
        return Daywork.builder()
                .schedule(schedule)
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .dateTime(dto.getDateTime())
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.status = Workcheck.NOT_COMPLETE;
    }

    public void updateDaywork(DayworkDto dto) {
        this.title = dto.getTitle();
        this.status = dto.getStatus();
        this.priority = dto.getPriority();
        this.dateTime.changeDateTime(dto.getDateTime().getHour(), dto.getDateTime().getMinute());
    }
}

