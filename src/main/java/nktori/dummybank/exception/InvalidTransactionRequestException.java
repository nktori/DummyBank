package nktori.dummybank.exception;

public class InvalidTransactionRequestException extends IllegalArgumentException {
    public InvalidTransactionRequestException(String message) {
        super(message);
    }
}
