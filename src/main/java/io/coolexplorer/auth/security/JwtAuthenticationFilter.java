package io.coolexplorer.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtTokenProvider.getToken(request);

        if (StringUtils.isNotEmpty(token)) {
            token = jwtTokenProvider.resolveToken(request);
            Authentication authentication = null;
            try {
                authentication = jwtTokenProvider.getAuthentication(token);
            } catch (Exception e) {
                LOGGER.debug("Cannot extract Authentication from Jwt.", e);
            }

            if (authentication != null && jwtTokenProvider.isValid(token, request)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, servletResponse);
    }
}
