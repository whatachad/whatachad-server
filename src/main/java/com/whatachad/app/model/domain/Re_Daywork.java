package com.whatachad.app.model.domain;

import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Re_Daywork extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "DAYWORK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAYSCHEDULE_ID")
    private Re_DaySchedule daySchedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private DayworkPriority priority;

    @Enumerated(EnumType.STRING)
    private Workcheck status;

    private Integer hour;

    private Integer minute;

    /* 연관관계 편의 메소드 */
    public void addDaySchedule(Re_DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
        daySchedule.getDayworks().add(this);
    }
}