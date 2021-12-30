package io.coolexplorer.auth.service;

import io.coolexplorer.auth.model.Account;

public interface AuthService {
    Account login(String email, String password);
}
