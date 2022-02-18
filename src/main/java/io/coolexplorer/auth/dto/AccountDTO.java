package io.coolexplorer.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.coolexplorer.auth.annotations.ClassFieldConstraint;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.modelmapper.ModelMapper;

import javax.management.relation.RoleInfo;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        @Schema
        @JsonProperty("roles")
        private Set<RoleInfo> roles = new HashSet<>();

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
    @Schema(description = "Role Info")
    @JsonPropertyOrder({"role", "description"})
    public static class RoleInfo {
        @Schema(example = "ROLE_USER")
        @JsonProperty("role")
        private String roleName = "";

        @Schema(example = "General User")
        private String description = "";
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

        @Schema(example = "role")
        @NotNull(message = "{signup.role.empty}")
        private String role;

        public Account toAccount(Role role) {
            Account account = new Account()
                    .setEmail(email)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setPassword(password);

            account.addRole(role);

            return account;
        }
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

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Account Get List Query Parameters")
    public static class AccountListParams {
        @Schema(example = "userId|name|cellPhone|..")
        @ClassFieldConstraint(object = Account.class, message = "{field.name.not.valid}")
        private String searchField;

        @Schema(example = "searchKeyword")
        private String searchQuery;

        @Schema(example = "userId|name|cellPhone|..")
        @ClassFieldConstraint(object = Account.class, message = "{field.name.not.valid}")
        private String orderBy;

        @Schema(example = "ASC|DESC")
        private String order;

        @Schema(example = "ROLE_USER")
        private String role;

        @Schema(example = "1")
        private Integer page;

        @Schema(example = "10")
        private Integer itemsPerPage;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Account List Response")
    public static class AccountListResponse {
        @Schema(example = "[AccountDTO.AccountInfo, AccountDTO.AccountInfo]")
        private List<AccountInfo> accounts = new ArrayList<>();

        @Schema(example = "PageInfo")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private PageInfo pageInfo;
    }
}
