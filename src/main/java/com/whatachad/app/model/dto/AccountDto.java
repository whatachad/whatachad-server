package com.whatachad.app.model.dto;

import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountDto {

    private Long id;
    private String title;
    private AccountType type;
    private AccountCategory category;
    private Integer cost;

}
