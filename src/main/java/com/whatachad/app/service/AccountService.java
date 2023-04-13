package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(AccountDto accountDto) {
        return accountRepository.save(Account.create(accountDto));
    }

    @Transactional
    public void updateAccount(AccountDto accountDto, Long account_id) {
        Account findAccount = accountRepository.findById(account_id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "account"));

        findAccount.updateAccount(accountDto);
    }

    @Transactional(readOnly = true)
    public Account findAccountById(Long account_id) {
        return accountRepository.findById(account_id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "account"));
    }

}
