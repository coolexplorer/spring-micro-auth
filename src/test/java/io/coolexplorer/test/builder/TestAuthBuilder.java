package io.coolexplorer.test.builder;

import io.coolexplorer.auth.dto.AuthDTO;

public class TestAuthBuilder {
    public static Long ID = 1L;
    public static String EMAIL = "test@coolexplorer.io";
    public static String PASSWORD = "test";
    public static String TOKEN = "testToken";

    public static AuthDTO.LoginRequest defaultLoginRequest() {
        return new AuthDTO.LoginRequest()
                .setEmail(EMAIL)
                .setPassword(PASSWORD);
    }
}
