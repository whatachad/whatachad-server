package com.whatachad.app.model.domain;

import com.whatachad.app.type.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private Integer cost;

    @Embedded
    private DateTime dateTime;
}
