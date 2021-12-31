package io.coolexplorer.auth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.coolexplorer.auth.model.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AccountDTO {
    private AccountDTO() {
        throw new IllegalStateException("AccountDTO");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Account Info")
    @JsonPropertyOrder()
    public static class AccountInfo {
        @Schema(example = "1L")
        private Long id;

        @Schema(example = "email@email.com")
        private String email;

        @Schema(example = "John")
        private String firstName;

        @Schema(example = "Kim")
        private String lastName;

        public static AccountInfo from(Account account, ModelMapper modelMapper) {
            return modelMapper.map(account, AccountInfo.class);
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Account creation request")
    public static class AccountCreationRequest {
        @Schema(example = "email@email.com")
        @NotBlank(message = "{account.email.empty}")
        @Email(message = "{account.invalid.email}")
        private String email;

        @Schema(example = "John")
        @NotBlank(message = "{account.first.name.empty}")
        private String firstName;

        @Schema(example = "Kim")
        @NotBlank(message = "{account.last.name.empty}")
        private String lastName;

        @Schema(example = "testPassword")
        @NotBlank(message = "{account.password.empty}")
        private String password;
    }
}
