package com.whatachad.app.model.request;

import com.whatachad.app.type.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateAccountRequestDto {

    @NotBlank
    private String title;
    @NotNull
    private AccountType type;
    @NotBlank
    private String category;
    @NotNull
    private Integer cost;
    @NotNull
    private Integer date;

}
