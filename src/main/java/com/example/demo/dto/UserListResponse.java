package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    @Schema(description = "List of users", required = true)
    private List<UserResponse> users;

    @Schema(description = "Current page number", example = "0", required = true)
    private int page;

    @Schema(description = "Page size", example = "20", required = true)
    private int size;

    @Schema(description = "Total number of elements", example = "100", required = true)
    private long totalElements;

    @Schema(description = "Total number of pages", example = "5", required = true)
    private int totalPages;

    @Schema(description = "Whether this is the first page", example = "true", required = true)
    private boolean first;

    @Schema(description = "Whether this is the last page", example = "false", required = true)
    private boolean last;
}