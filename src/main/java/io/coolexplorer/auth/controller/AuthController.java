package io.coolexplorer.auth.controller;

import io.coolexplorer.auth.dto.AccountDTO;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"auth"})
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Operation(summary = "Create Account", description = "Create Account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountDTO.AccountInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    // TODO : Temporal annotation for Swagger OAS3.0 bug
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(code=400, message = "Bad Request", response = ErrorResponse.class)
    )
    @GetMapping("/accounts/{id}")
    public AccountDTO.AccountInfo createAccount(@PathVariable("id") String id) {
        return new AccountDTO.AccountInfo();
    }

}
