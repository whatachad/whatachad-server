package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import com.whatachad.app.util.EntityUtils;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAY_SCHEDULE_ID")
    private DaySchedule daySchedule;

    private String title;

    private Integer cost;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    public static Account create(AccountDto dto) {
        return Account.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .cost(dto.getCost())
                .type(dto.getType())
                .category(dto.getCategory())
                .build();
    }

    public void update(AccountDto dto) {
        EntityUtils.setValueExceptNull(this, dto);
    }

}