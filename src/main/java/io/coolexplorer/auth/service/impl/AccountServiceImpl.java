package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.repository.AccountRepository;
import io.coolexplorer.auth.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Account Creation DataIntegrityViolationException {}", e);

            // TODO: Add custom exception
        }

        // TODO: Delete this line when throw custom exception
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
