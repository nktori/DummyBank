package nktori.dummybank.service;

import nktori.dummybank.builders.AccountBuilder;
import nktori.dummybank.builders.TransactionBuilder;
import nktori.dummybank.builders.TransactionOutputBuilder;
import nktori.dummybank.builders.TransactionRequestBuilder;
import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.dto.TransactionRequest;
import nktori.dummybank.exception.AccountNotFoundException;
import nktori.dummybank.exception.InsufficientFundsException;
import nktori.dummybank.exception.InvalidTransactionRequestException;
import nktori.dummybank.model.Transaction;
import nktori.dummybank.repository.AccountRepository;
import nktori.dummybank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionApplicationServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private TransactionApplicationService transactionService;

    @Test
    public void getAllTransactionsReturnsEmptyWhenNoTransactions() {
        when(transactionRepository.findAll()).thenReturn(emptyList());

        assertThat(transactionService.getAllTransactions()).isEmpty();
    }

    @Test
    public void getAllTransactionsReturnsCorrectly() {
        List<Transaction> transactions = createTransactions();
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionOutput> actual = transactionService.getAllTransactions();

        List<TransactionOutput> expected = createTransactionOutputs();
        assertThat(actual).containsExactlyInAnyOrder(expected.get(0), expected.get(1));
    }

    @Test
    public void getAllTransactionsByIdReturnsCorrectly() {
        List<TransactionOutput> transactionOutputs = new ArrayList<>(createTransactionOutputs());
        transactionOutputs.add(
                TransactionOutputBuilder.builder()
                        .withPayeeId(1)
                        .withPayerId(3)
                        .withAmount(100)
                        .withTimestamp(LocalDateTime.MIN)
                        .build()
        );

        List<TransactionOutput> actual1 = transactionService.getTransactionsByAccountId(1, transactionOutputs);
        List<TransactionOutput> actual2 = transactionService.getTransactionsByAccountId(2, transactionOutputs);

        assertThat(actual1).containsExactlyInAnyOrder(transactionOutputs.get(0), transactionOutputs.get(1), transactionOutputs.get(2));
        assertThat(actual2).containsExactlyInAnyOrder(transactionOutputs.get(0), transactionOutputs.get(1));
    }

    @Test
    public void makeTransactionThrowsWhenAmountLessThanOne() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withFrom(1)
                .withTo(2)
                .withAmount(0)
                .build();

        assertThatThrownBy(() -> transactionService.makeTransaction(request)).isInstanceOf(InvalidTransactionRequestException.class);
    }

    @Test
    public void makeTransactionThrowsWhenPayerAndPayeeSame() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withFrom(1)
                .withTo(1)
                .withAmount(50)
                .build();

        assertThatThrownBy(() -> transactionService.makeTransaction(request)).isInstanceOf(InvalidTransactionRequestException.class);
    }

    @Test
    public void makeTransactionThrowsWhenPayerAccountNotFound() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withFrom(1)
                .withTo(2)
                .withAmount(50)
                .build();
        when(clock.instant()).thenReturn(LocalDateTime.MIN.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(accountRepository.findById(request.getFrom())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.makeTransaction(request)).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void makeTransactionThrowsWhenPayerHasInsufficientFunds() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withFrom(1)
                .withTo(2)
                .withAmount(50)
                .build();
        when(clock.instant()).thenReturn(LocalDateTime.MIN.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(accountRepository.findById(request.getFrom())).thenReturn(Optional.of(AccountBuilder.builder()
                .withId(request.getFrom())
                .withBalance(request.getAmount() - 1)
                .build()));

        assertThatThrownBy(() -> transactionService.makeTransaction(request)).isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    public void makeTransactionReturnsCorrectly() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withFrom(1)
                .withTo(2)
                .withAmount(50)
                .build();
        when(clock.instant()).thenReturn(LocalDateTime.MIN.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(accountRepository.findById(request.getFrom()))
                .thenReturn(Optional.of(AccountBuilder.builder()
                        .withId(request.getFrom())
                        .withBalance(request.getAmount())
                        .build()));
        doNothing().when(accountRepository).updateAccountBalanceById(anyInt(), anyInt());
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(TransactionBuilder.builder()
                        .withId(1)
                        .withPayeeId(request.getTo())
                        .withPayerId(request.getFrom())
                        .withAmount(request.getAmount())
                        .withTimestamp(LocalDateTime.MIN)
                        .build());

        TransactionOutput actual = transactionService.makeTransaction(request);

        verify(accountRepository).updateAccountBalanceById(eq(request.getFrom()), eq(request.getAmount() * -1));
        verify(accountRepository).updateAccountBalanceById(eq(request.getTo()), eq(request.getAmount()));
        verify(transactionRepository).save(any(Transaction.class));

        TransactionOutput expected = TransactionOutputBuilder.builder()
                .withPayeeId(request.getTo())
                .withPayerId(request.getFrom())
                .withAmount(request.getAmount())
                .withTimestamp(LocalDateTime.MIN)
                .build();
        assertThat(actual).isEqualTo(expected);
    }

    private List<Transaction> createTransactions() {
        return List.of(
                TransactionBuilder.builder()
                        .withId(1)
                        .withPayeeId(1)
                        .withPayerId(2)
                        .withAmount(100)
                        .withTimestamp(LocalDateTime.MIN)
                        .build(),
                TransactionBuilder.builder()
                        .withId(2)
                        .withPayeeId(2)
                        .withPayerId(1)
                        .withAmount(200)
                        .withTimestamp(LocalDateTime.MAX)
                        .build()
        );
    }

    private List<TransactionOutput> createTransactionOutputs() {
        return List.of(
                TransactionOutputBuilder.builder()
                        .withPayeeId(1)
                        .withPayerId(2)
                        .withAmount(100)
                        .withTimestamp(LocalDateTime.MIN)
                        .build(),
                TransactionOutputBuilder.builder()
                        .withPayeeId(2)
                        .withPayerId(1)
                        .withAmount(200)
                        .withTimestamp(LocalDateTime.MAX)
                        .build()
        );
    }
}
