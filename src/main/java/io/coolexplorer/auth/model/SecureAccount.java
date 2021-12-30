package io.coolexplorer.auth.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SecureAccount implements UserDetails {

    @Getter
    private Account account;
    private List<GrantedAuthority> authorities;

    public SecureAccount(Account account) {
        this.account = account;
    }

    private void setAuthorities(Account account) {
        authorities = new ArrayList<>();

        account.getRoles().forEach(
                accountRole -> authorities.add(new SimpleGrantedAuthority(accountRole.getRoleName()))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    public String getJwtToken() {
        return account.getJwtToken();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
