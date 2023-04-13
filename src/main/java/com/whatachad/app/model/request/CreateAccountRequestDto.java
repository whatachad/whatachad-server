package com.whatachad.app.model.request;

import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateAccountRequestDto {

    @NotNull
    private String title;
    @NotNull
    private AccountType type;
    @NotNull
    private String category;
    @NotNull
    private Integer cost;

    private Integer date;

    private Integer hour;

    private Integer minute;
}
