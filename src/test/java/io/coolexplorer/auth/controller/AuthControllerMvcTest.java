package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.AuthDTO;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import io.coolexplorer.auth.utils.DateTimeUtils;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestAuthBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yaml")
public class AuthControllerMvcTest extends SpringBootWebMvcTestSupport {
    @MockBean
    private AuthService authService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtTokenMessageService jwtTokenMessageService;

    Account defaultAccount;

    @BeforeEach
    void setUp() {
        defaultAccount = TestAccountBuilder.defaultAccount();
    }

    @Nested
    @DisplayName("Sign Up Test")
    class AuthSignUpTest {
        @Test
        @DisplayName("Success")
        void testSignUp() throws Exception {
            AccountDTO.AccountInfo accountInfo = AccountDTO.AccountInfo.from(defaultAccount, modelMapper);
            AccountDTO.AccountCreationRequest accountCreationRequest = TestAccountBuilder.defaultCreationRequest();

            when(authService.signup(any())).thenReturn(defaultAccount);

            String payload = objectMapper.writeValueAsString(accountCreationRequest);
            String expectedResponse = objectMapper.writeValueAsString(accountInfo);

            mockMvc.perform(post("/api/v1/signup")
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

    @Nested
    @DisplayName("Login Test")
    class AuthLoginTest {
        @Test
        @DisplayName("Success")
        void testLogin() throws Exception {
            AuthDTO.LoginRequest loginRequest = TestAuthBuilder.defaultLoginRequest();
            AuthDTO.TokenInfo tokenInfo = new AuthDTO.TokenInfo()
                    .setJwtToken(TestAuthBuilder.TOKEN);

            Account accountWithToken = TestAccountBuilder.accountWithToken();

            String payload = objectMapper.writeValueAsString(loginRequest);
            String expectedResponse = objectMapper.writeValueAsString(tokenInfo);

            when(authService.login(any(), any())).thenReturn(accountWithToken);
            doNothing().when(jwtTokenMessageService).creteJwtTokenCache(any());
            when(jwtTokenProvider.getExpiredDate(any())).thenReturn(DateUtils.addMinutes(new Date(), 3));

            mockMvc.perform(post("/api/v1/login")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

}
