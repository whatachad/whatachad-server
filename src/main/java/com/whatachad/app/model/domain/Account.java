package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private int cost;

    private int time;

    private int date;
}
