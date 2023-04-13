package com.whatachad.app.model.response;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAccountResponseDto {
    private String title;
    private Integer cost;
    private AccountType type;
    private String category;
    private DateTime dateTime;
}
