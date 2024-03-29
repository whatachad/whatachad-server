package com.whatachad.app.type;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@AllArgsConstructor
@Getter
public enum AccountCategory {
    SALARY("월급", AccountType.INCOME),
    INTEREST("이자", AccountType.INCOME),
    ALLOWANCE("용돈", AccountType.INCOME),

    FOOD("식비", AccountType.SPEND),
    RESIDENCE("거주비", AccountType.SPEND),
    INSURANCE("보험", AccountType.SPEND);

    private final String label;
    private final AccountType type;

    private static final Map<String, AccountCategory> BY_LABEL = new HashMap<>();
    private static final List<AccountCategory> INCOME_CATEGORY = new ArrayList<>();
    private static final List<AccountCategory> SPEND_CATEGORY = new ArrayList<>();

    static {
        for (AccountCategory c : values()) {
            BY_LABEL.put(c.label, c);
            if (c.type.equals(AccountType.INCOME)) {
                INCOME_CATEGORY.add(c);
            } else {
                SPEND_CATEGORY.add(c);
            }
        }
    }

    public static AccountCategory valueOfLabel(AccountType type, String label) {
        AccountCategory accountCategory = BY_LABEL.get(label);
        if (accountCategory == null) {
            throw new CommonException(BError.NOT_VALID, "account category");
        }
        if (accountCategory.getType() != type) {
            throw new CommonException(BError.NOT_MATCHES, "account type", "account category");
        }
        return accountCategory;
    }

}
