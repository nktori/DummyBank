package nktori.dummybank.controller;

import nktori.dummybank.dto.AccountOutput;
import nktori.dummybank.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/{accountId}")
    public AccountOutput getAccountById(@PathVariable Integer accountId) {
        return accountService.getAccountById(accountId);
    }
}
