package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.test.builder.TestAccountBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yaml")
public class AccountControllerMvcTest extends SpringBootWebMvcTestSupport {
    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    Account dtoAccount;
    Account defaultAccount;

    @BeforeEach
    void setUp() {
        dtoAccount = TestAccountBuilder.dtoAccount();
        defaultAccount = TestAccountBuilder.defaultAccount();
    }

    @Nested
    @DisplayName("Account Creation Test")
    class AccountCreationTest {
        @Test
        @DisplayName("Success")
        void testCreateAccount() throws Exception {
            AccountDTO.AccountInfo accountInfo = AccountDTO.AccountInfo.from(defaultAccount, modelMapper);
            AccountDTO.AccountCreationRequest accountCreationRequest = TestAccountBuilder.defaultCreationRequest();

            when(accountService.create(any())).thenReturn(defaultAccount);

            String payload = objectMapper.writeValueAsString(accountCreationRequest);
            String expectedResponse = objectMapper.writeValueAsString(accountInfo);

            mockMvc.perform(post("/api/v1/account")
                        .with(user("test").password("1234").roles("USER"))
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

    @Nested
    @DisplayName("Account Retrieve Test")
    class AccountRetrieveTest {
        @Test
        @DisplayName("Success")
        void testRetrieveAccount() throws Exception {
            AccountDTO.AccountInfo accountInfo = AccountDTO.AccountInfo.from(defaultAccount, modelMapper);

            when(accountService.getAccount(anyLong())).thenReturn(defaultAccount);

            String expectedResponse = objectMapper.writeValueAsString(accountInfo);

            mockMvc.perform(get("/api/v1/account/1")
                            .with(user("test").password("1234").roles("USER")))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

    @Nested
    @DisplayName("Account Update Test")
    class AccountUpdateTest {
        @Test
        @DisplayName("Success")
        void testUpdateAccount() throws Exception {
            AccountDTO.AccountInfo accountInfo = AccountDTO.AccountInfo.from(defaultAccount, modelMapper);
            AccountDTO.AccountUpdateRequest updateRequest = modelMapper.map(dtoAccount, AccountDTO.AccountUpdateRequest.class);

            String payload = objectMapper.writeValueAsString(updateRequest);
            String expectedResponse = objectMapper.writeValueAsString(accountInfo);

            when(accountService.getAccount(anyLong())).thenReturn(defaultAccount);
            when(accountService.update(any())).thenReturn(defaultAccount);

            mockMvc.perform(put("/api/v1/account/1")
                            .with(user("test").password("1234").roles("USER"))
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

    @Nested
    @DisplayName("Account Deletion Test")
    class AccountDeletionTest {
        @Test
        @DisplayName("Success")
        void testDeletionAccount() throws Exception {
            when(accountService.getAccount(anyLong())).thenReturn(defaultAccount);

            accountService.delete(TestAccountBuilder.ID);

            mockMvc.perform(delete("/api/v1/account/1")
                            .with(user("test").password("1234").roles("USER")))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
        }
    }
}
