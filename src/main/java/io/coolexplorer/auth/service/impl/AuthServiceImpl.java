package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.enums.ExceptionCode;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MessageSourceAccessor errorMessageSourceAccessor;

    @Override
    public Account login(String email, String password) {
        Account account = accountService.getAccount(email);

        if (account == null) {
            throw new UsernameNotFoundException(errorMessageSourceAccessor.getMessage(ExceptionCode.USERNAME_NOT_FOUND.getMessageKey()));
        }

        if (passwordEncoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException(errorMessageSourceAccessor.getMessage(ExceptionCode.BAD_CREDENTIALS.getMessageKey()));
        }

        account.setJwtToken(jwtTokenProvider.createJwtToken(account));
        account.setLastLogin(LocalDateTime.now());

        return accountService.update(account);
    }

    @Override
    public Account signup(Account account) throws UserDataIntegrityViolationException {
        return accountService.create(account);
    }

    @Override
    public Account refreshToken(String email) {
        Account account = accountService.getAccount(email);
        account.setJwtToken(jwtTokenProvider.createJwtToken(account));

        return accountService.update(account);
    }

    @Override
    public void deleteToken(String email) {
        Account account = accountService.getAccount(email);

        if (account == null) {
            throw new UsernameNotFoundException(errorMessageSourceAccessor.getMessage(ExceptionCode.USERNAME_NOT_FOUND.getMessageKey()));
        }

        account.setJwtToken("");
        accountService.update(account);
    }
}