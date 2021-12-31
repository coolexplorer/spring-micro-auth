package io.coolexplorer.test.builder;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.model.Account;

public class TestAccountBuilder {
    public static Long ID = 1L;
    public static String EMAIL = "test@coolexplorer.io";
    public static String FIRST_NAME= "John";
    public static String LAST_NAME = "Kim";
    public static String PASSWORD = "test";

    public static Account baseAccount() {
        return new Account()
                .setEmail(EMAIL)
                .setPassword(PASSWORD)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .addRole(TestRoleBuilder.userRole());
    }

    public static Account defaultAccount() {
        return baseAccount()
                .setId(ID);
    }

    public static Account accountWithToken() {
        return baseAccount()
                .setId(ID)
                .setJwtToken(TestAuthBuilder.TOKEN);
    }

    public static Account dtoAccount() {
        return baseAccount();
    }

    public static AccountDTO.AccountCreationRequest defaultCreationRequest() {
        return new AccountDTO.AccountCreationRequest()
                .setEmail(EMAIL)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPassword(PASSWORD);
    }
}
