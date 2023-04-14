package com.whatachad.app.model.request;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateAccountRequestDto {

    private String title;
    private AccountType type;
    private String category;
    private Integer cost;
    private Integer hour;
    private Integer minute;
}
