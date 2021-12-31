package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.dto.AccountDTO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

            mockMvc.perform(post("/api/v1/account")
                    .with(user("test").password("1234").roles("USER"))
                    .content(objectMapper.writeValueAsString(accountCreationRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(objectMapper.writeValueAsString(accountInfo)));
        }
    }
}
