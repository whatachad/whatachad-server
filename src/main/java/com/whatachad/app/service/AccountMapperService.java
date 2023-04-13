package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.response.CreateAccountResponseDto;
import com.whatachad.app.type.AccountCategory;
import org.springframework.stereotype.Service;

@Service
public class AccountMapperService {
    public AccountDto toAccountDto(CreateAccountRequestDto dto, Integer date) {
        return AccountDto.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .category(AccountCategory.valueOfLabel(dto.getCategory()))
                .cost(dto.getCost())
                .dateTime(createDateTime(dto, date))
                .build();
    }

    private DateTime createDateTime(CreateAccountRequestDto dto, Integer date) {
        return DateTime.builder()
                .hour(dto.getHour())
                .minute(dto.getMinute())
                .date(date)
                .build();
    }

    public CreateAccountResponseDto toCreateAccountResponseDto(Account account) {
        return CreateAccountResponseDto.builder()
                .title(account.getTitle())
                .type(account.getType())
                .category(account.getCategory().getLabel())
                .cost(account.getCost())
                .dateTime(account.getDateTime())
                .build();
    }
}
