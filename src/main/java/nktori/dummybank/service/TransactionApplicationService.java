package nktori.dummybank.service;

import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.dto.TransactionRequest;
import nktori.dummybank.exception.AccountNotFoundException;
import nktori.dummybank.exception.InsufficientFundsException;
import nktori.dummybank.exception.InvalidTransactionRequestException;
import nktori.dummybank.model.Account;
import nktori.dummybank.model.Transaction;
import nktori.dummybank.repository.AccountRepository;
import nktori.dummybank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionApplicationService implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final Clock clock;

    public TransactionApplicationService(AccountRepository accountRepository, TransactionRepository transactionRepository, Clock clock) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }

    @Override
    public List<TransactionOutput> getAllTransactions() {
        return transactionRepository.findAll().stream().map(TransactionOutput::of).collect(Collectors.toList());
    }

    @Override
    public List<TransactionOutput> getTransactionsByAccountId(Integer id, List<TransactionOutput> transactions) {
        return transactions.stream()
                .filter(t -> t.getPayerId().equals(id) || t.getPayeeId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionOutput makeTransaction(TransactionRequest request) {
        if (request.getAmount() <= 0)
            throw new InvalidTransactionRequestException("Invalid transaction amount. Cannot be less than 1");
        if (request.getFrom().equals(request.getTo()))
            throw new InvalidTransactionRequestException("Payer and Payee cannot have the same id");
        Transaction transaction = new Transaction(request.getTo(), request.getFrom(), request.getAmount(), LocalDateTime.now(clock));

        Account payer = accountRepository.findById(request.getFrom()).orElseThrow(() -> new AccountNotFoundException(request.getFrom()));
        if (payer.getBalance() < transaction.getAmount()) throw new InsufficientFundsException(payer.getId());

        accountRepository.updateAccountBalanceById(transaction.getPayeeId(), transaction.getAmount());
        accountRepository.updateAccountBalanceById(transaction.getPayerId(), (transaction.getAmount() * -1));
        return TransactionOutput.of(transactionRepository.save(transaction));
    }
}
