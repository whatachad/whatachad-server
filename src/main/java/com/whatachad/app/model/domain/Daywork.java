package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.vo.BaseTime;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

import static com.whatachad.app.util.EntityUtil.setValueExceptNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Daywork extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAYWORK_ID")
    private Long id;

    private LocalDate dayworkDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAY_SCHEDULE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DaySchedule daySchedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private DayworkPriority priority;

    @Enumerated(EnumType.STRING)
    private Workcheck status;

    public static Daywork create(DayworkDto dto) {
        return Daywork.builder()
                .dayworkDate(dto.getDayworkDate())
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .status(Workcheck.NOT_COMPLETE)
                .build();
    }

    public void update(DayworkDto dto) {
        setValueExceptNull(this, dto);
    }

    public void setDayworkDate(Integer year, Integer month, Integer day) {
        this.dayworkDate = LocalDate.of(year, month, day);
    }

}