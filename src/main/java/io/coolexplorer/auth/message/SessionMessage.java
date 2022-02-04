package io.coolexplorer.auth.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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
    @JsonInclude(NON_NULL)
    @Schema(description = "Session Info")
    public static class SessionInfo {
        @Schema(example = "ff6681f0-50f8-4110-bf96-ef6cec45780e")
        private String id;

        @Schema(example = "1L")
        private Long accountId;

        @Schema(example = "{\"orderCount\":1}")
        private String values;
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

        @Schema(example = "10L")
        private Long expiration = -1L;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Session Retrieve Message")
    public static class RequestMessage {
        @Schema(example = "1L")
        private Long accountId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Session Update Message")
    public static class UpdateMessage {
        @Schema(example = "1L")
        private Long accountId;

        @Schema(example = "{\"orderCount\":1}")
        private String values;

        @Schema(example = "10L")
        private Long expiration = -1L;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "Session Deletion Message")
    public static class DeleteMessage {
        @Schema(example = "1L")
        private Long accountId;
    }
}
