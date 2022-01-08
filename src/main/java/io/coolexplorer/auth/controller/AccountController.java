package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Create Account", description = "Create Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/account")
    public AccountDTO.AccountInfo createAccount(@Valid @RequestBody AccountDTO.AccountCreationRequest request) throws UserDataIntegrityViolationException {
        Account account = modelMapper.map(request, Account.class);
        Account createdAccount = accountService.create(account);

        return AccountDTO.AccountInfo.from(createdAccount, modelMapper);
    }

    @Operation(summary = "Create Account", description = "Create Account", responses = {
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

    @Operation(summary = "Create Account", description = "Create Account", responses = {
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

    @Operation(summary = "Create Account", description = "Create Account", responses = {
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
