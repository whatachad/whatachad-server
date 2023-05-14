package com.whatachad.app.model.vo;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AccountDayworkByDay {
    private LocalDate date;
    private List<Account> accounts;
    private List<Daywork> dayworks;

    public AccountDayworkByDay(LocalDate date, List<Account> accounts, List<Daywork> dayworks) {
        // LAZY LOADING 하기 위해 ArrayList 생성자로 객체 내용에 접근
        this.date = date;
        this.accounts = new ArrayList<>(accounts);
        this.dayworks = new ArrayList<>(dayworks);
    }
}
