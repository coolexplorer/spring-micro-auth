package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.dto.PageInfo;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.filter.AccountSearchFilter;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.model.Role;
import io.coolexplorer.auth.service.AccountService;
import io.coolexplorer.auth.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Create Account", description = "Create Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/account")
    public AccountDTO.AccountInfo createAccount(@Valid @RequestBody AccountDTO.AccountCreationRequest request) throws UserDataIntegrityViolationException {
        Role role = roleService.getRole(request.getRole());
        Account account = request.toAccount(role);
        Account createdAccount = accountService.create(account);

        return AccountDTO.AccountInfo.from(createdAccount, modelMapper);
    }

    @Operation(summary = "Get Account", description = "Get Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/account/{id}")
    public AccountDTO.AccountInfo getAccount(@Valid @PathVariable("id") Long id) throws UserNotFoundException {
        Account account = accountService.getAccount(id);

        return AccountDTO.AccountInfo.from(account, modelMapper);
    }

    @Operation(summary = "Get Account", description = "Get Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/account")
    public AccountDTO.AccountListResponse getAccountList(@Valid AccountDTO.AccountListParams params) {
        AccountSearchFilter filter = modelMapper.map(params, AccountSearchFilter.class)
                .setPage(params.getPage())
                .setItemsPerPage(params.getItemsPerPage());

        Page<Account> page = accountService.getAccounts(filter);

        if (page != null) {
            List<AccountDTO.AccountInfo> accountInfoList = page.getContent()
                    .stream()
                    .map(account -> modelMapper.map(account,AccountDTO.AccountInfo.class))
                    .collect(Collectors.toList());

            PageInfo pageInfo = new PageInfo()
                    .setTotalCount(page.getTotalElements())
                    .setTotalPages(page.getTotalPages());

            return new AccountDTO.AccountListResponse()
                    .setAccounts(accountInfoList)
                    .setPageInfo(pageInfo);
        }

        return null;
    }

    @Operation(summary = "Update Account", description = "Update Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/account/{id}")
    public AccountDTO.AccountInfo updateAccount(@Valid @PathVariable Long id, @Valid @RequestBody AccountDTO.AccountUpdateRequest request) throws UserNotFoundException {
        Account account = accountService.getAccount(id);
        modelMapper.map(request, account);

        Account updatedAccount = accountService.update(account);

        return AccountDTO.AccountInfo.from(updatedAccount, modelMapper);
    }

    @Operation(summary = "Delete Account", description = "Delete Account", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/account/{id}")
    public ResponseEntity<String> deleteAccount(@Valid @PathVariable("id") Long id) throws UserNotFoundException {
        accountService.delete(id);

        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
