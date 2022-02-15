package nktori.dummybank.service;

import nktori.dummybank.builders.AccountBuilder;
import nktori.dummybank.builders.TransactionOutputBuilder;
import nktori.dummybank.dto.AccountOutput;
import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.exception.AccountNotFoundException;
import nktori.dummybank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {

    private static final Integer ID = 1;
    private static final Integer BALANCE = 500;

    @Mock
    private AccountRepository repository;

    @Mock
    private TransactionApplicationService transactionService;

    @InjectMocks
    private AccountApplicationService accountService;

    @Test
    public void noAccountFoundThrowsCorrectly() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(ID)).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void accountWithNoTransactionsIsSuccessfullyReturned() {
        when(repository.findById(ID)).thenReturn(Optional.of(AccountBuilder.builder().withId(ID).withBalance(BALANCE).build()));
        when(transactionService.getAllTransactions()).thenReturn(emptyList());

        AccountOutput actual = accountService.getAccountById(ID);

        AccountOutput expected = new AccountOutput(ID, BALANCE, emptyList());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void accountWithTransactionsIsSuccessfullyReturned() {
        when(repository.findById(ID)).thenReturn(Optional.of(AccountBuilder.builder().withId(ID).withBalance(BALANCE).build()));
        List<TransactionOutput> transactionOutputs = List.of(
                TransactionOutputBuilder.builder()
                        .withPayeeId(ID)
                        .withPayerId(ID + 1)
                        .withAmount(20)
                        .withTimestamp(LocalDateTime.MIN)
                        .build(),
                TransactionOutputBuilder.builder()
                        .withPayeeId(ID + 1)
                        .withPayerId(ID)
                        .withAmount(10)
                        .withTimestamp(LocalDateTime.MAX)
                        .build()
        );
        when(transactionService.getAllTransactions()).thenReturn(transactionOutputs);
        when(transactionService.getTransactionsByAccountId(eq(ID), eq(transactionOutputs))).thenCallRealMethod();

        AccountOutput actual = accountService.getAccountById(ID);

        AccountOutput expected = new AccountOutput(ID, BALANCE, transactionOutputs);
        assertThat(actual).isEqualTo(expected);
    }
}
