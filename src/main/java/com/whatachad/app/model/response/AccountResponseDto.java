package com.whatachad.app.model.response;

import com.whatachad.app.type.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String title;
    private Integer cost;
    private AccountType type;
    private String category;
    private LocalDate accountDate;
}
