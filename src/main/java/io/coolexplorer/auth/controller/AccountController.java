package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
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
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/account")
    public AccountDTO.AccountInfo createAccount(@Valid @RequestBody AccountDTO.AccountCreationRequest request) {
        Account account = modelMapper.map(request, Account.class);
        Account createdAccount = accountService.create(account);

        return AccountDTO.AccountInfo.from(createdAccount, modelMapper);
    }
}
