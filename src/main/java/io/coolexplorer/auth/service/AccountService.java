package io.coolexplorer.auth.service;

import io.coolexplorer.auth.model.Account;

public interface AccountService {
    Account create(Account account);
    Account getAccount(String email);
    Account update(Account account);
}
