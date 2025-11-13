package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserListPageRequestDto {

    @Schema(description = "Current page number in the user list", example = "1", required = false)
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Long pageNumber;

    @Schema(description = "User ID of the first record on the current page", example = "USER001", required = false)
    @Size(max = 8, message = "First user ID must not exceed 8 characters")
    private String firstUserId;

    @Schema(description = "User ID of the last record on the current page", example = "USER010", required = false)
    @Size(max = 8, message = "Last user ID must not exceed 8 characters")
    private String lastUserId;

    @Schema(description = "Indicates whether more pages exist after the current page", example = "true", required = false)
    private Boolean hasNextPage;

    @Schema(description = "Number of user records displayed per page", example = "10", required = false)
    @Min(value = 1, message = "Records per page must be at least 1")
    @Max(value = 100, message = "Records per page cannot exceed 100")
    private Integer recordsPerPage;
}
