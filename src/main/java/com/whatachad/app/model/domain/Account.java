package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.vo.BaseTime;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import com.whatachad.app.util.EntityUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    private LocalDate accountDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAY_SCHEDULE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DaySchedule daySchedule;

    private String title;

    private Integer cost;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    public static Account create(AccountDto dto) {
        return Account.builder()
                .accountDate(dto.getAccountDate())
                .title(dto.getTitle())
                .cost(dto.getCost())
                .type(dto.getType())
                .category(dto.getCategory())
                .build();
    }

    public void update(AccountDto dto) {
        EntityUtil.setValueExceptNull(this, dto);
    }

}