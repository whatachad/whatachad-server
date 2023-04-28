package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.response.AccountResponseDto;
import com.whatachad.app.type.AccountCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountConverter {

    private final AccountMapper accountMapper;

    public AccountDto toAccountDto(CreateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getCategory()));
        return accountDto;
    }

    public AccountDto toAccountDto(UpdateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getCategory()));
        return accountDto;
    }

    public AccountResponseDto toAccountResponseDto(Account account) {
        return accountMapper.toAccountResponseDto(account);
    }
}
