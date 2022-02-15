package nktori.dummybank.service;

import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.dto.TransactionRequest;

import java.util.List;

public interface TransactionService {
    List<TransactionOutput> getAllTransactions();

    List<TransactionOutput> getTransactionsByAccountId(Integer id, List<TransactionOutput> transactions);

    TransactionOutput makeTransaction(TransactionRequest request);
}
