package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.AuthDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "User Login", description = "User Login", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public AuthDTO.TokenInfo getToken(@Valid @RequestBody AuthDTO.LoginRequest request) {
        Account account = authService.login(request.getEmail(), request.getPassword());

        return new AuthDTO.TokenInfo(account.getJwtToken());
    }
}
