package io.coolexplorer.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Parameter Validation Result")
public class ValidationResult {
    @Schema(example = "email")
    private String field;

    @Schema(example = "code")
    private String code;

    @Schema(example = "Input a valid email")
    private String description;
}
