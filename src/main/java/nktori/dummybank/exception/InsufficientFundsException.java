package nktori.dummybank.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Integer accountId) {
        super(String.format("Account Id#%d has insufficient funds for this transaction", accountId));
    }
}
