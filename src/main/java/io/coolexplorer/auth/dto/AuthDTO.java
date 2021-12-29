package io.coolexplorer.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class AuthDTO {
    public AuthDTO() {
        throw new IllegalStateException("AuthDTO");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Token Info")
    public static class TokenInfo {
        @Schema(example = "token.example")
        private String token;
    }
}
