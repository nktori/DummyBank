package nktori.dummybank.dto;

import nktori.dummybank.model.Account;

import java.util.List;
import java.util.Objects;

public class AccountOutput {

    private final Integer id;
    private final Integer balance;
    private final List<TransactionOutput> transactions;

    public AccountOutput(Integer id, Integer balance, List<TransactionOutput> transactions) {
        this.id = id;
        this.balance = balance;
        this.transactions = transactions;
    }

    public static AccountOutput from(Account account, List<TransactionOutput> transactions) {
        return new AccountOutput(
                account.getId(),
                account.getBalance(),
                transactions
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountOutput that = (AccountOutput) o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, transactions);
    }

    public Integer getId() {
        return id;
    }

    public Integer getBalance() {
        return balance;
    }

    public List<TransactionOutput> getTransactions() {
        return transactions;
    }
}
