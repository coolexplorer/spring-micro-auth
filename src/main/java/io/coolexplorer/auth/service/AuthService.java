package io.coolexplorer.auth.service;

import io.coolexplorer.auth.enums.RoleType;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.model.Account;

public interface AuthService {
    Account login(String email, String password);
    Account signup(Account account, RoleType roleType) throws UserDataIntegrityViolationException;
    Account refreshToken(String email);
    void deleteToken(String email) throws UserNotFoundException;
}
