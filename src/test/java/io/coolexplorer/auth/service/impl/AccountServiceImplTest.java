package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.repository.AccountRepository;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.test.builder.TestAccountBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    Account dtoAccount;
    Account defaultAccount;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository);

        dtoAccount = TestAccountBuilder.dtoAccount();
        defaultAccount = TestAccountBuilder.defaultAccount();
    }

    @Nested
    @DisplayName("Account Creation Test")
    class AccountCreationTest {
        @Test
        @DisplayName("Success")
        void testCreateAccount() {
            when(accountRepository.save(any())).thenReturn(defaultAccount);

            Account createdAccount = accountService.create(dtoAccount);

            assertThat(createdAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }

    @Nested
    @DisplayName("Account Retrieve Test")
    class AccountRetrieveTest {
        @Test
        @DisplayName("Success")
        void testRetrieveAccount() {
            when(accountRepository.findAccountByEmail(any())).thenReturn(defaultAccount);

            Account retrieveAccount = accountService.getAccount(TestAccountBuilder.EMAIL);

            assertThat(retrieveAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }

    @Nested
    @DisplayName("Account Update Test")
    class AccountUpdateTest {
        @Test
        @DisplayName("Success")
        void testUpdateAccount() {
            when(accountRepository.save(any())).thenReturn(defaultAccount);

            Account updatedAccount = accountService.update(dtoAccount);

            assertThat(updatedAccount).isNotNull().isEqualTo(defaultAccount);
        }
    }
}
