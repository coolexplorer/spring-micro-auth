package io.coolexplorer.auth.security;

import io.coolexplorer.auth.enums.ErrorCode;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.model.SecureAccount;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestJwtTokenProviderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

import static io.coolexplorer.auth.security.JwtTokenProvider.AUTH_HEADER;
import static io.coolexplorer.auth.security.JwtTokenProvider.AUTH_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private HttpServletRequest request;

    private JwtTokenProvider jwtTokenProvider;

    String secretKey = "test-secret";
    private int tokenValidMinutes = 30; // 30 minutes

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                userDetailsService,
                secretKey,
                tokenValidMinutes
        );
    }

    @Nested
    @DisplayName("JwtToken Creation Test")
    class JwtTokenCreationTest {
        @Test
        @DisplayName("Success with username and roles")
        void testCreateJwtTokenWithUsernameAndRoles() {
            String jwtToken = jwtTokenProvider.createJwtToken(TestJwtTokenProviderBuilder.EMAIL, TestJwtTokenProviderBuilder.ROLES);

            LOGGER.debug("Email: {}, Role: {}, JwtToken: {}", TestJwtTokenProviderBuilder.EMAIL, TestJwtTokenProviderBuilder.ROLES, jwtToken);

            assertThat(TestJwtTokenProviderBuilder.EMAIL).isEqualTo(jwtTokenProvider.getEmail(jwtToken));
            assertThat(TestJwtTokenProviderBuilder.ROLES).containsExactlyInAnyOrderElementsOf(jwtTokenProvider.getRoles(jwtToken));
        }

        @Test
        @DisplayName("Success with account")
        void testCreateJwtTokenWithAccount() {
            Account account = TestAccountBuilder.defaultAccount();
            String jwtToken = jwtTokenProvider.createJwtToken(account);

            LOGGER.debug("Account: {}, JwtToken: {}", account, jwtToken);

            assertThat(account.getEmail()).isEqualTo(jwtTokenProvider.getEmail(jwtToken));
            assertThat(TestJwtTokenProviderBuilder.ROLES).containsExactlyInAnyOrderElementsOf(jwtTokenProvider.getRoles(jwtToken));
        }
    }

    @Nested
    @DisplayName("Authentication Retrieve Test")
    class JwtTokenAuthenticationRetrieveTest {
        @Test
        @DisplayName("Success")
        void testRetrieveAuthenticationTest() {
            String jwtToken = jwtTokenProvider.createJwtToken(TestAccountBuilder.defaultAccount());

            when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn(TestAccountBuilder.EMAIL);

            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();

            assertThat(userDetails.getUsername()).isEqualTo(TestAccountBuilder.EMAIL);
        }
    }

    @Nested
    @DisplayName("Resolve JwtToken Test")
    class JwtTokenResolveTest {
        @Test
        @DisplayName("Success")
        void testResolveJwtTokenTest() {
            String jwtToken = jwtTokenProvider.createJwtToken(TestAccountBuilder.defaultAccount());

            when(request.getHeader(AUTH_HEADER)).thenReturn(AUTH_PREFIX + jwtToken);

            String resolvedToken = jwtTokenProvider.resolveToken(request);

            assertThat(resolvedToken).isEqualTo(jwtToken);
        }
    }

    @Nested
    @DisplayName("JwtToken Validation Test")
    class JwtTokenValidationTest {
        @Test
        @DisplayName("Success")
        void testValidateJwtToken() {
            String jwtToken = jwtTokenProvider.createJwtToken(TestAccountBuilder.defaultAccount());

            SecureAccount secureAccount = new SecureAccount(TestAccountBuilder.defaultAccount().setJwtToken(jwtToken));

            when(userDetailsService.loadUserByUsername(any())).thenReturn(secureAccount);

            assertThat(jwtTokenProvider.isValid(jwtToken, request)).isTrue();
        }

        @Test
        @DisplayName("Failed with timeout")
        void testValidateJwtTokenWithTimeout() throws InterruptedException {
            int tokenValidMillis = -100;
            jwtTokenProvider = new JwtTokenProvider(userDetailsService, secretKey, tokenValidMillis);
            String token = jwtTokenProvider.createJwtToken(TestAccountBuilder.defaultAccount());

            TimeUnit.MILLISECONDS.sleep(200L);

            assertThat(jwtTokenProvider.isValid(token, request)).isFalse();
            verify(request).setAttribute(eq(ErrorCode.JWT_TOKEN_EXPIRED.toString()), any(String.class));
        }

        @Test
        @DisplayName("Invalid with timeout and null request object")
        void testTokenExpiredWithoutRequest() throws Exception {
            int tokenValidMillis = 100;
            jwtTokenProvider = new JwtTokenProvider(userDetailsService, secretKey, tokenValidMillis);
            String jwtToken = jwtTokenProvider.createJwtToken(TestAccountBuilder.defaultAccount());

            TimeUnit.MILLISECONDS.sleep(200L);

            assertThat(jwtTokenProvider.isValid(jwtToken, null)).isFalse();
            verify(request, times(0)).setAttribute(eq(ErrorCode.JWT_TOKEN_EXPIRED.toString()), eq(null));
        }
    }
}
