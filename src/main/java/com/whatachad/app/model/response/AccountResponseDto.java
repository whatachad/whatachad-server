package com.whatachad.app.model.response;

import com.whatachad.app.type.AccountType;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String title;
    private Integer cost;
    private AccountType type;
    private String category;
    private LocalDate accountDate;
}
