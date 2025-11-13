package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListPageResponseDto {

    @Schema(description = "Unique identifier for the page")
    private Long id;

    @Schema(description = "Current page number in the user list", example = "1")
    private Long pageNumber;

    @Schema(description = "User ID of the first record on the current page", example = "USER001")
    private String firstUserId;

    @Schema(description = "User ID of the last record on the current page", example = "USER010")
    private String lastUserId;

    @Schema(description = "Indicates whether more pages exist after the current page", example = "true")
    private Boolean hasNextPage;

    @Schema(description = "Number of user records displayed per page", example = "10")
    private Integer recordsPerPage;

    @Schema(description = "Indicates if there is a previous page", example = "false")
    private Boolean hasPreviousPage;

    @Schema(description = "Next page number if available", example = "2")
    private Long nextPageNumber;

    @Schema(description = "Previous page number if available", example = "0")
    private Long previousPageNumber;

    @Schema(description = "Indicates if the page is empty", example = "false")
    private Boolean isEmpty;

    @Schema(description = "Number of records on this page", example = "10")
    private Integer recordCount;

    @Schema(description = "Timestamp when the page was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the page was last updated")
    private LocalDateTime updatedAt;
}
