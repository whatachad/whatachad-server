package com.whatachad.app.model.response;

import com.whatachad.app.type.AccountType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAccountResponseDto {
    private String title;
    private Integer cost;
    private AccountType type;
    private String category;
    private Integer year;
    private Integer month;
    private Integer date;
}
