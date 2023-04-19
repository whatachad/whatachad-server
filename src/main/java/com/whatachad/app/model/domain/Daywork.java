package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Daywork extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "DAYWORK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAYSCHEDULE_ID")
    private DaySchedule daySchedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private DayworkPriority priority;

    @Enumerated(EnumType.STRING)
    private Workcheck status;

    private Integer hour;

    private Integer minute;

    public static Daywork create(DayworkDto dto) {
        return Daywork.builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .hour(dto.getHour())
                .minute(dto.getMinute())
                .build();
    }

    public void updateDaywork(DayworkDto dto) {
        this.title = dto.getTitle();
        this.priority = dto.getPriority();
        this.status = dto.getStatus();
        this.hour = dto.getHour();
        this.minute = dto.getMinute();
    }

    @PrePersist
    public void prePersist() {
        this.status = Workcheck.NOT_COMPLETE;
    }
    /* 연관관계 편의 메소드 */

    public void addDaySchedule(DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
        daySchedule.getDayworks().add(this);
    }
}