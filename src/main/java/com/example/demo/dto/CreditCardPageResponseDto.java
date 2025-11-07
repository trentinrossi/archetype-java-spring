package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for paginated credit card response
 * Business Rule BR002: The system displays a maximum of 7 credit card records per page.
 * Business Rule BR005: The system maintains page state including current page number,
 * first and last card keys displayed, and whether additional pages exist.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardPageResponseDto {
    
    @Schema(description = "List of credit cards on current page (max 7 records)", 
            example = "[]")
    private List<CreditCardResponseDto> creditCards;
    
    @Schema(description = "Current page number (0-indexed)", example = "0")
    private Integer currentPage;
    
    @Schema(description = "Total number of pages", example = "5")
    private Integer totalPages;
    
    @Schema(description = "Total number of records matching filter", example = "35")
    private Long totalRecords;
    
    @Schema(description = "Number of records per page (max 7)", example = "7")
    private Integer pageSize;
    
    @Schema(description = "Card number of first card on current page", example = "1234567890123456")
    private String firstCardKey;
    
    @Schema(description = "Card number of last card on current page", example = "1234567890123499")
    private String lastCardKey;
    
    @Schema(description = "Whether there are more pages after current page", example = "true")
    private Boolean hasNextPage;
    
    @Schema(description = "Whether there are pages before current page", example = "false")
    private Boolean hasPreviousPage;
    
    @Schema(description = "Whether current page is the first page", example = "true")
    private Boolean isFirstPage;
    
    @Schema(description = "Whether current page is the last page", example = "false")
    private Boolean isLastPage;
}
