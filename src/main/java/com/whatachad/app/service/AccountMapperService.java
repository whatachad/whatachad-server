package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.response.CreateAccountResponseDto;
import com.whatachad.app.model.response.UpdateAccountResponseDto;
import com.whatachad.app.type.AccountCategory;
import org.springframework.stereotype.Service;

@Service
public class AccountMapperService {
    public AccountDto toAccountDto(CreateAccountRequestDto dto) {
        return AccountDto.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .category(AccountCategory.valueOfLabel(dto.getCategory()))
                .cost(dto.getCost())
                .build();
    }

    public AccountDto toAccountDto(UpdateAccountRequestDto dto) {
        return AccountDto.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .category(AccountCategory.valueOfLabel(dto.getCategory()))
                .cost(dto.getCost())
                .build();
    }

    public CreateAccountResponseDto toCreateAccountResponseDto(Account account) {
        return CreateAccountResponseDto.builder()
                .title(account.getTitle())
                .type(account.getType())
                .category(account.getCategory().getLabel())
                .cost(account.getCost())
                .year(account.getAccountDate().getYear())
                .month(account.getAccountDate().getMonthValue())
                .date(account.getAccountDate().getDayOfMonth())
                .build();
    }

    public UpdateAccountResponseDto toUpdateResponseDto(Account account) {
        return UpdateAccountResponseDto.builder()
                .title(account.getTitle())
                .type(account.getType())
                .category(account.getCategory().getLabel())
                .cost(account.getCost())
                .year(account.getAccountDate().getYear())
                .month(account.getAccountDate().getMonthValue())
                .date(account.getAccountDate().getDayOfMonth())
                .build();
    }
}
