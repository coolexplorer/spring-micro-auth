package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.test.builder.TestAccountBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AccountService accountService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageSourceAccessor errorMessageSourceAccessor;

    private AuthService authService;

    Account defaultAccount;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                accountService,
                jwtTokenProvider,
                passwordEncoder,
                errorMessageSourceAccessor
        );

        defaultAccount = TestAccountBuilder.defaultAccount();
    }

    @Nested
    @DisplayName("Login Test")
    class AuthLoginTest{
        @Test
        @DisplayName("Success")
        void testLogin() {
            when(accountService.getAccount(anyString())).thenReturn(defaultAccount);
            when(jwtTokenProvider.createJwtToken(any())).thenReturn("testToken");
            when(accountService.update(any())).thenReturn(defaultAccount);

            Account returnAccount = authService.login(TestAccountBuilder.EMAIL, TestAccountBuilder.PASSWORD);

            assertThat(returnAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }
}
