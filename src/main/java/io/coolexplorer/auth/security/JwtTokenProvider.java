package io.coolexplorer.auth.security;

import io.coolexplorer.auth.enums.ErrorCode;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.model.SecureAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final String secretKey;
    private final int tokenValidMinutes;

    public JwtTokenProvider(
            UserDetailsService userDetailsService,
            @Value("${spring.jwt.secretKey}") String secretKey,
            @Value("${spring.jwt.tokenValidMinutes}") int tokenValidMinutes) {
        this.userDetailsService = userDetailsService;
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tokenValidMinutes = tokenValidMinutes;
    }

    public String createJwtToken(String username, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addMinutes(now, tokenValidMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createJwtToken(Account account) {
        Claims claims = Jwts.claims().setSubject(account.getEmail());
        claims.put("roles", account.getRolesAsString());

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addMinutes(now, tokenValidMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER).replace(AUTH_PREFIX, "");
    }

    public boolean isValid(String jwtToken, HttpServletRequest request) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            SecureAccount secureAccount = (SecureAccount) userDetailsService.loadUserByUsername(getUsername(jwtToken));
            String token = secureAccount.getJwtToken();

            boolean isSameToken = token != null && token.equals(jwtToken);

            return !claims.getBody().getExpiration().before(new Date()) && isSameToken;
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT Token is expired.", e);

            if (request != null) {
                request.setAttribute(ErrorCode.JWT_TOKEN_EXPIRED.toString(), e.getMessage());
            }

            return false;
        } catch (Exception e) {
            LOGGER.error("JWT Token is not valid.", e);

            if (request != null) {
                request.setAttribute(ErrorCode.JWT_TOKEN_INVALID.toString(), e.getMessage());
            }

            return false;
        }

    }
}
