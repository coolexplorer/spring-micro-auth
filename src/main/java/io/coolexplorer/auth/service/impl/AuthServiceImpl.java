package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.enums.ExceptionCode;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
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
}