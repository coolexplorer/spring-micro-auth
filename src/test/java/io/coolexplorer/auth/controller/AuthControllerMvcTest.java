package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.coolexplorer.auth.dto.AuthDTO;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestAuthBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    Account defaultAccount;

    @BeforeEach
    void setUp() {
        defaultAccount = TestAccountBuilder.defaultAccount();
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

            mockMvc.perform(post("/api/v1/login")
                        .with(user("test").password("1234").roles("USER"))
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(expectedResponse));
        }
    }

}
