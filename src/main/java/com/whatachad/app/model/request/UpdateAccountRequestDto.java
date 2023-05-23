package com.whatachad.app.model.request;

import com.whatachad.app.type.AccountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAccountRequestDto {

    @NotBlank(message = "{account.title.blank}")
    @Size(min = 1, message = "{account.title.size}")
    @Size(max = 50, message = "{account.title.size}")
    private String title;

    @NotNull(message = "{account.type.null}")
    private AccountType type;

    @NotBlank(message = "{account.category.blank}")
    private String category;

    @NotNull(message = "{account.cost.null}")
    @Min(value = 0, message = "{account.cost.range}")
    @Max(value = 99999999, message = "{account.cost.range}")
    private Integer cost;

}
