package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(AccountDto accountDto) {
        return accountRepository.save(Account.create(accountDto));
    }
}
