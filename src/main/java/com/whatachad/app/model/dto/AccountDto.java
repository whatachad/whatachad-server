package com.whatachad.app.model.dto;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccountDto {

    private Long id;
    private String title;
    private AccountType type;
    private AccountCategory category;
    private Integer cost;
    private DateTime dateTime;
}
