package nktori.dummybank.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Integer accountId) {
        super(String.format("Account Id#%d not found", accountId));
    }
}
