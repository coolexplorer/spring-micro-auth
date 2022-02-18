package io.coolexplorer.auth.service;

import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.filter.AccountSearchFilter;
import io.coolexplorer.auth.model.Account;
import org.springframework.data.domain.Page;

public interface AccountService {
    Account create(Account account) throws UserDataIntegrityViolationException;
    Account getAccount(String email);
    Account getAccount(Long id) throws UserNotFoundException;
    Page<Account> getAccounts(AccountSearchFilter filter);
    Account update(Account account);
    void delete(Long id) throws UserNotFoundException;
}
