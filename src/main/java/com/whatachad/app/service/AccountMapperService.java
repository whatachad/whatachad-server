package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.response.CreateAccountResponseDto;
import com.whatachad.app.model.response.UpdateAccountResponseDto;
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

    public AccountDto toAccountDto(UpdateAccountRequestDto dto) {
        return AccountDto.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .category(AccountCategory.valueOfLabel(dto.getCategory()))
                .cost(dto.getCost())
                .dateTime(createDateTime(dto))
                .build();
    }

    private DateTime createDateTime(CreateAccountRequestDto dto, Integer date) {
        return DateTime.builder()
                .hour(dto.getHour())
                .minute(dto.getMinute())
                .date(date)
                .build();
    }

    private DateTime createDateTime(UpdateAccountRequestDto dto) {
        return DateTime.builder()
                .hour(dto.getHour())
                .minute(dto.getMinute())
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

    public UpdateAccountResponseDto toUpdateResponseDto(Account account) {
        return UpdateAccountResponseDto.builder()
                .title(account.getTitle())
                .type(account.getType())
                .category(account.getCategory().getLabel())
                .cost(account.getCost())
                .dateTime(account.getDateTime())
                .build();
    }
}
