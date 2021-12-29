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
        @Schema(example = "John")
        private String firstName;

        @Schema(example = "Kim")
        private String lastName;

        @Schema(example = "email@email.com")
        private String email;
    }
}
