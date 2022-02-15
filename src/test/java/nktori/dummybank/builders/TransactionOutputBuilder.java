package nktori.dummybank.builders;

import nktori.dummybank.dto.TransactionOutput;

import java.time.LocalDateTime;

public class TransactionOutputBuilder {

    private Integer payerId;
    private Integer payeeId;
    private Integer amount;
    private LocalDateTime timestamp;

    public static TransactionOutputBuilder builder() {
        return new TransactionOutputBuilder();
    }

    public TransactionOutputBuilder withPayerId(Integer payerId) {
        this.payerId = payerId;
        return this;
    }

    public TransactionOutputBuilder withPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
        return this;
    }

    public TransactionOutputBuilder withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public TransactionOutputBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TransactionOutput build() {
        return new TransactionOutput(payeeId, payerId, amount, timestamp);
    }
}
