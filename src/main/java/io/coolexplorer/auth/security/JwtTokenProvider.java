package io.coolexplorer.auth.security;

import io.jsonwebtoken.Claims;
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
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {
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

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return "";
    }

    public boolean isValid(String jwtToken, HttpServletRequest request) {
        return true;
    }
}
