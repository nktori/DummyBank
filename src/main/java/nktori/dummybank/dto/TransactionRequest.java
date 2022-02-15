package nktori.dummybank.dto;

public class TransactionRequest {

    private final Integer to;
    private final Integer from;
    private final Integer amount;

    public TransactionRequest(Integer to, Integer from, Integer amount) {
        this.to = to;
        this.from = from;
        this.amount = amount;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getAmount() {
        return amount;
    }
}
