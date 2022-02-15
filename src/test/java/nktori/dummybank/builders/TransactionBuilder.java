package nktori.dummybank.builders;

import nktori.dummybank.model.Transaction;

import java.time.LocalDateTime;

public class TransactionBuilder {

    private Integer id;
    private Integer payerId;
    private Integer payeeId;
    private Integer amount;
    private LocalDateTime timestamp;

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public TransactionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder withPayerId(Integer payerId) {
        this.payerId = payerId;
        return this;
    }

    public TransactionBuilder withPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
        return this;
    }

    public TransactionBuilder withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Transaction build() {
        Transaction transaction = new Transaction(payeeId, payerId, amount, timestamp);
        transaction.setId(id);
        return transaction;
    }
}
