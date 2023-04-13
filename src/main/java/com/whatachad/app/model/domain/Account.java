package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.type.AccountCategory;
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

    private String title;

    private Integer cost;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    @Embedded
    private DateTime dateTime;

    public static Account create(AccountDto accountDto) {
        return Account.builder()
                .title(accountDto.getTitle())
                .cost(accountDto.getCost())
                .type(accountDto.getType())
                .category(accountDto.getCategory())
                .dateTime(accountDto.getDateTime())
                .build();
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addScheduleInAccount(Schedule schedule) {
        this.schedule = schedule;
        schedule.getAccounts().add(this);
    }

}
