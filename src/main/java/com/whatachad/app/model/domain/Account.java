package com.whatachad.app.model.domain;

import com.whatachad.app.model.enums.AccountType;
import com.whatachad.app.model.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account_table")
public class Account {

    @Id
    @Column(name = "account_id")
    private String id;

    @Column(name = "account_year")
    private String year;

    @Column(name = "account_moth")
    private String month;

    @Column(name = "account_day")
    private String day;

    @Column
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // 완료상태 [EXPENDITURE, IMCOME]

    @Column
    private int amount;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

}
