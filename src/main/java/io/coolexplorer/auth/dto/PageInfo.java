package io.coolexplorer.auth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@Schema(name = "PageInfo", description = "Page Info")
@JsonPropertyOrder({"totalCount", "totalPage"})
public class PageInfo {
    @Schema(example = "10")
    private Long totalCount = 0L;

    @Schema(example = "1")
    private Integer totalPages = 0;
}
