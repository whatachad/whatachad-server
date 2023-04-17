package com.whatachad.app.model.request;

import com.whatachad.app.type.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAccountRequestDto {

    private String title;
    private AccountType type;
    private String category;
    private Integer cost;

}
