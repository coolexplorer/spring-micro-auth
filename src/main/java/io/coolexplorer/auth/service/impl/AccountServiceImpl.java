package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
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
    public Account create(Account account) throws UserDataIntegrityViolationException {
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Account Creation DataIntegrityViolationException {}", e);

            throw new UserDataIntegrityViolationException();
        }
    }

    @Override
    public Account getAccount(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account getAccount(Long id) throws UserNotFoundException {
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null) {
            throw new UserNotFoundException();
        }

        return account;
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) throws UserNotFoundException {
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null) {
            throw new UserNotFoundException();
        }

        accountRepository.deleteById(id);
    }
}
