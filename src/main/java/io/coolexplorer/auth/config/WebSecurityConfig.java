package io.coolexplorer.auth.config;

import io.coolexplorer.auth.security.JwtAuthenticationFilter;
import io.coolexplorer.auth.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MessageSourceAccessor errorMessageSourceAccessor;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
            .httpBasic()
                .disable()
            .formLogin()
                .disable()
            .headers()
                .frameOptions()
                .sameOrigin()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/v2/api-docs/**", "/v3/api-docs/**", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .antMatchers("**/auth/token").permitAll()
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint(errorMessageSourceAccessor))
                .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
