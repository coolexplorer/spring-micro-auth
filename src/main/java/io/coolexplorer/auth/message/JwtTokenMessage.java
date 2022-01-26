package io.coolexplorer.auth.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class JwtTokenMessage {
    public JwtTokenMessage() {
        new IllegalStateException("JwtTokenMessage");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "JwtToken Cache Creation Message")
    public static class CreateMessage {
        @Schema(example = "1L")
        private Long accountId;
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.....")
        private String jwtToken;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "JwtToken Cache Retrieve Message")
    public static class RetrieveMessage {
        @Schema(example = "ff6681f0-50f8-4110-bf96-ef6cec45780e")
        private String id;
        @Schema(example = "1L")
        private Long accountId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "JwtToken Cache Update Message")
    public static class UpdateMessage {
        @Schema(example = "ff6681f0-50f8-4110-bf96-ef6cec45780e")
        private String id;
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.....")
        private String jwtToken;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "JwtToken Cache Delete Message")
    public static class DeleteMessage {
        @Schema(example = "ff6681f0-50f8-4110-bf96-ef6cec45780e")
        private String id;
        @Schema(example = "1L")
        private Long accountId;
    }
}
