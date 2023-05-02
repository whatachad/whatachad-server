package com.whatachad.app.type;

import lombok.Getter;

@Getter
public enum AccountType {

    SPEND(0),
    INCOME(1);

    private final int value;

    AccountType(int value) {
        this.value = value;
    }
}
