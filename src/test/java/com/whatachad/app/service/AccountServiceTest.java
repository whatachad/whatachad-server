package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.repository.AccountRepository;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    private Account accountEx;

    @BeforeEach
    void initAccount() {
        Account account = Account.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        accountEx = accountRepository.save(account);
    }

    @Test
    @DisplayName("가계부 내용을 수정한다.")
    void updateAccount() {
        AccountDto accountDto = AccountDto.builder()
                .title("장보기")
                .cost(8000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        accountService.updateAccount(accountDto, accountEx.getId());

        Account findAccount = accountService.findAccountById(accountEx.getId());
        assertThat(findAccount.getCost()).isEqualTo(8000);
    }
}
