package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.enums.RoleType;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.auth.service.RoleService;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestAuthBuilder;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AccountService accountService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageSourceAccessor errorMessageSourceAccessor;

    private AuthService authService;

    Account dtoAccount;
    Account defaultAccount;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                accountService,
                roleService,
                jwtTokenProvider,
                passwordEncoder,
                errorMessageSourceAccessor
        );

        dtoAccount = TestAccountBuilder.dtoAccount();
        defaultAccount = TestAccountBuilder.defaultAccount();
    }

    @Nested
    @DisplayName("Login Test")
    class AuthLoginTest{
        @Test
        @DisplayName("Success")
        void testLogin() {
            when(accountService.getAccount(anyString())).thenReturn(defaultAccount);
            when(jwtTokenProvider.createJwtToken(any())).thenReturn(TestAuthBuilder.TOKEN);
            when(passwordEncoder.matches(any(), any())).thenReturn(true);
            when(accountService.update(any())).thenReturn(defaultAccount);

            Account returnAccount = authService.login(TestAccountBuilder.EMAIL, TestAccountBuilder.PASSWORD);

            assertThat(returnAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }

    @Nested
    @DisplayName("Auth Sign Up Test")
    class AuthSignUpTest {
        @Test
        @DisplayName("Success")
        void testSignUp() throws UserDataIntegrityViolationException {
            when(accountService.create(any())).thenReturn(defaultAccount);

            Account createdAccount = authService.signup(dtoAccount, RoleType.ROLE_USER);

            AssertionsForClassTypes.assertThat(createdAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }

    @Nested
    @DisplayName("Auth refresh token Test")
    class AuthRefreshTokenTest {
        @Test
        @DisplayName("Success")
        void testRefreshToken() {
            when(accountService.getAccount(anyString())).thenReturn(defaultAccount);
            when(jwtTokenProvider.createJwtToken(any())).thenReturn(TestAuthBuilder.TOKEN);
            when(accountService.update(any())).thenReturn(defaultAccount);

            Account createdAccount = authService.refreshToken(TestAccountBuilder.EMAIL);

            AssertionsForClassTypes.assertThat(createdAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }

    @Nested
    @DisplayName("Auth Token Deletion Test")
    class AuthTokenDeletionTest {
        @Test
        @DisplayName("Success")
        void testDeleteToken() throws UserNotFoundException {
            when(accountService.getAccount(anyString())).thenReturn(defaultAccount);
            when(accountService.update(any())).thenReturn(defaultAccount);

            authService.deleteToken(TestAccountBuilder.EMAIL);

            verify(accountService).update(defaultAccount);
        }
    }
}
