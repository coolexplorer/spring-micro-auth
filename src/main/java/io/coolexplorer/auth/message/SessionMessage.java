package io.coolexplorer.auth.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class SessionMessage {

    public SessionMessage() {
        new IllegalStateException("SessionMessage");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Session Creation Message")
    public static class CreateMessage {
        @Schema(example = "1L")
        private Long accountId;
        @Schema(example = "{\"orderCount\":1}")
        private String values;
    }
}
