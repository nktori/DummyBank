package nktori.dummybank.builders;

import nktori.dummybank.dto.TransactionRequest;

public class TransactionRequestBuilder {

    private Integer to;
    private Integer from;
    private Integer amount;

    public static TransactionRequestBuilder builder() {
        return new TransactionRequestBuilder();
    }

    public TransactionRequestBuilder withTo(Integer to) {
        this.to = to;
        return this;
    }

    public TransactionRequestBuilder withFrom(Integer from) {
        this.from = from;
        return this;
    }

    public TransactionRequestBuilder withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public TransactionRequest build() {
        return new TransactionRequest(to, from, amount);
    }
}
