package com.whatachad.app.model.response;

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
    private Integer year;
    private Integer month;
    private Integer date;
}
