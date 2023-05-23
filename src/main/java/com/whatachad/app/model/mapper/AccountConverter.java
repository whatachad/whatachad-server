package com.whatachad.app.model.mapper;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.response.AccountResponseDto;
import com.whatachad.app.type.AccountCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AccountConverter {

    private final AccountMapper accountMapper;

    public AccountDto toAccountDto(String yearAndMonth, Integer day, CreateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        int year = Integer.parseInt(yearAndMonth.substring(0, 4));
        int month = Integer.parseInt(yearAndMonth.substring(4));
        try {
            accountDto.setAccountDate(LocalDate.of(year, month, day));
        } catch (DateTimeException e) {
            throw new CommonException(BError.NOT_VALID, "day");
        }
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getType(), dto.getCategory()));
        return accountDto;
    }

    public AccountDto toAccountDto(UpdateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getType(), dto.getCategory()));
        return accountDto;
    }

    public AccountResponseDto toAccountResponseDto(Account account) {
        return accountMapper.toAccountResponseDto(account);
    }
}
