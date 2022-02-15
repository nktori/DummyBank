package nktori.dummybank.service;

import nktori.dummybank.dto.AccountOutput;
import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.exception.AccountNotFoundException;
import nktori.dummybank.model.Account;
import nktori.dummybank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountApplicationService implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountApplicationService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public AccountOutput getAccountById(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        List<TransactionOutput> allTransactions = transactionService.getAllTransactions();
        return AccountOutput.from(account, transactionService.getTransactionsByAccountId(id, allTransactions));
    }
}
