package com.whatachad.app.model.dto;

import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AccountDto {

    private Long id;
    private String title;
    private AccountType type;
    private AccountCategory category;
    private Integer cost;

}
