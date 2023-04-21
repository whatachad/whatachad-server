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

import java.util.List;

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
    public void updateAccount(AccountDto accountDto, Long accountId) {
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "account"));

        findAccount.update(accountDto);
    }

    @Transactional(readOnly = true)
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "account"));
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsByDayId(Long dayScheduleId) {
        return accountRepository.findByDaySchedule_Id(dayScheduleId);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
}
