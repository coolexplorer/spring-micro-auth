package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.AuthDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.message.SessionMessage;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.service.AuthService;
import io.coolexplorer.auth.service.SessionMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final SessionMessageService sessionMessageService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Create Account", description = "Create Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")
    public AccountDTO.AccountInfo createAccount(@Valid @RequestBody AccountDTO.AccountCreationRequest request) throws UserDataIntegrityViolationException {
        Account account = modelMapper.map(request, Account.class);
        Account createdAccount = authService.signup(account);

        SessionMessage.CreateMessage sessionData = new SessionMessage.CreateMessage()
                .setAccountId(1L)
                .setValues("{\"loginCount\":10}");
        sessionMessageService.createSession(sessionData);

        return AccountDTO.AccountInfo.from(createdAccount, modelMapper);
    }

    @Operation(summary = "User Login", description = "User Login", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthDTO.TokenInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public AuthDTO.TokenInfo login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        Account account = authService.login(request.getEmail(), request.getPassword());

        return new AuthDTO.TokenInfo(account.getJwtToken());
    }

    @Operation(summary = "Token refresh", description = "Token refresh", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthDTO.TokenInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/auth/refresh/token")
    public AuthDTO.TokenInfo refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = authService.refreshToken(authentication.getName());

        return new AuthDTO.TokenInfo(account.getJwtToken());
    }

    @Operation(summary = "Token deletion", description = "Token Deletion", responses = {
            @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(example = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/auth/token")
    public ResponseEntity<String> deleteToken() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.deleteToken(authentication.getName());

        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
