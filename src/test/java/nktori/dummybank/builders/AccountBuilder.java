package nktori.dummybank.builders;

import nktori.dummybank.model.Account;

public class AccountBuilder {

    private Integer id;
    private Integer balance;

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public AccountBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public AccountBuilder withBalance(Integer balance) {
        this.balance = balance;
        return this;
    }

    public Account build() {
        return new Account(id, balance);
    }
}
