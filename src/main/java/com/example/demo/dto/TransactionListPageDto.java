package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BR001: Transaction List Pagination - Represents a page of transaction records for display
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListPageDto {

    @Schema(description = "Current page number being displayed", example = "1", required = true)
    private Integer pageNumber;

    @Schema(description = "Transaction ID of the first record on the current page", example = "1234567890123456")
    private String firstTransactionId;

    @Schema(description = "Transaction ID of the last record on the current page", example = "1234567890123466")
    private String lastTransactionId;

    @Schema(description = "Indicates if there are more pages available", example = "true", required = true)
    private Boolean hasNextPage;

    @Schema(description = "Indicates if there is a previous page available", example = "false", required = true)
    private Boolean hasPreviousPage;

    @Schema(description = "Transaction ID selected by the user for detailed view", example = "1234567890123456")
    private String selectedTransactionId;

    @Schema(description = "Flag indicating user selection action (S for select)", example = "S")
    private String selectionFlag;

    @Schema(description = "List of transactions on this page (maximum 10)")
    private List<TransactionResponseDto> transactions;

    @Schema(description = "Total number of transactions available")
    private Long totalTransactions;

    @Schema(description = "Total number of pages available")
    private Integer totalPages;
}
