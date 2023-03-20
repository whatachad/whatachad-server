package com.whatachad.app.model.domain;

import com.whatachad.app.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class DayWork {
    @Id
    @Column(name = "account_id")
    private String id;

    @Column
    private String title;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private AccountType type; // 완료상태 [COMPLETE, NOT_COMPLETED]

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
