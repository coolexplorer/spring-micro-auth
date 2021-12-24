package io.coolexplorer.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.coolexplorer.auth.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(NON_NULL)
@Schema(description = "Error response")
@JsonPropertyOrder({"code", "description", "validationResults"})
public class ErrorResponse {
    @Schema(example = "error.auth.user.not.found")
    private ErrorCode code;

    @Schema(example = "Cannot find the user")
    private String description;

    @ArraySchema(schema = @Schema(implementation = ValidationResult.class))
    private List<ValidationResult> validationResults;
}
