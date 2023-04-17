package com.whatachad.app.model.domain;

import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Re_Account extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAYSCHEDULE_ID")
    private Re_DaySchedule daySchedule;

    private String title;

    private Integer cost;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    /* 연관관계 편의 메소드 */
    public void addDaySchedule(Re_DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
        daySchedule.getAccounts().add(this);
    }
}