package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.repository.AccountRepository;
import io.coolexplorer.auth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }
}
