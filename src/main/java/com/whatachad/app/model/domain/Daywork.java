package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import com.whatachad.app.util.EntityUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import static com.whatachad.app.util.EntityUtils.*;

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
    @JoinColumn(name = "DAY_SCHEDULE_ID")
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

    public void update(DayworkDto dto) {
        setValueExceptNull(this, dto);
    }

}