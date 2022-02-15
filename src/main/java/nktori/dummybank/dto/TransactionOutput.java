package nktori.dummybank.dto;

import nktori.dummybank.model.Transaction;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionOutput {

    private final Integer payeeId;
    private final Integer payerId;
    private final Integer amount;
    private final LocalDateTime timestamp;

    public TransactionOutput(Integer payeeId, Integer payerId, Integer amount, LocalDateTime timestamp) {
        this.payeeId = payeeId;
        this.payerId = payerId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public static TransactionOutput of(Transaction transaction) {
        return new TransactionOutput(
                transaction.getPayeeId(),
                transaction.getPayerId(),
                transaction.getAmount(),
                transaction.getTimestamp());
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public Integer getPayerId() {
        return payerId;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionOutput that = (TransactionOutput) o;
        return Objects.equals(payeeId, that.payeeId) && Objects.equals(payerId, that.payerId) && Objects.equals(amount, that.amount) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payeeId, payerId, amount, timestamp);
    }
}
