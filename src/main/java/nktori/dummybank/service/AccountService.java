package nktori.dummybank.service;

import nktori.dummybank.dto.AccountOutput;

public interface AccountService {
    AccountOutput getAccountById(Integer id);
}
