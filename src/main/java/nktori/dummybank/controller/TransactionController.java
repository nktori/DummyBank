package nktori.dummybank.controller;

import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.dto.TransactionRequest;
import nktori.dummybank.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public TransactionOutput makeTransaction(@RequestBody TransactionRequest request) {
        return transactionService.makeTransaction(request);
    }
}
