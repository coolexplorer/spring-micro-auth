package io.coolexplorer.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.time.LocalDateTime;

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
    @JsonPropertyOrder({"id", "email", "firstname", "lastname", "createdDate", "modifiedDate", "lastLogin"})
    public static class AccountInfo {
        @Schema(example = "1L")
        private Long id;

        @Schema(example = "email@email.com")
        private String email;

        @Schema(example = "John")
        private String firstName;

        @Schema(example = "Kim")
        private String lastName;

        @Schema(example = "2021-01-01T00:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDateTime createdDate;

        @Schema(example = "2021-01-01T00:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDateTime modifiedDate;

        @Schema(example = "2021-01-01T00:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDateTime lastLogin;

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

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Account update request")
    public static class AccountUpdateRequest {
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
    }
}
