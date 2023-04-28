package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.response.AccountResponseDto;
import com.whatachad.app.type.AccountCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "category", ignore = true)
    AccountDto preprocess(CreateAccountRequestDto dto);

    @Mapping(target = "category", ignore = true)
    AccountDto preprocess(UpdateAccountRequestDto dto);

    @Mapping(source = "category.label", target = "category")
    AccountResponseDto toAccountResponseDto(Account account);

    default AccountDto toAccountDto(CreateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getCategory()));
        return accountDto;
    }

    default AccountDto toAccountDto(UpdateAccountRequestDto dto) {
        AccountDto accountDto = accountMapper.preprocess(dto);
        accountDto.setCategory(AccountCategory.valueOfLabel(dto.getCategory()));
        return accountDto;
    }

}