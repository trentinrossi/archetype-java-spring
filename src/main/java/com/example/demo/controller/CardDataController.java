package com.example.demo.controller;

import com.example.demo.dto.CardDataDto;
import com.example.demo.service.CardDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Card Data Controller
 * REST API controller for CardData operations
 * Based on COBOL CBACT02C.cbl business rules for card data file processing
 */
@RestController
@RequestMapping("/api/v1/card-data")
@Validated
@Tag(name = "Card Data", description = "Card Data File Reader and Printer API - Based on COBOL CBACT02C.cbl")
public class CardDataController {

    private static final Logger logger = LoggerFactory.getLogger(CardDataController.class);

    private final CardDataService cardDataService;

    @Autowired
    public CardDataController(CardDataService cardDataService) {
        this.cardDataService = cardDataService;
    }

    /**
     * Read all card data records sequentially
     * Equivalent to the main processing functionality in COBOL CBACT02C.cbl
     */
    @GetMapping("/read-all")
    @Operation(
        summary = "Read all card data records sequentially",
        description = "Reads and returns all card data records in sequential order, equivalent to COBOL CBACT02C.cbl main processing loop"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all card data records",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardDataDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while reading card data")
    })
    public ResponseEntity<List<CardDataDto>> readAllCardData() {
        logger.info("REST API: Reading all card data records sequentially");
        
        try {
            List<CardDataDto> cardDataList = cardDataService.readAllCardDataSequentially();
            logger.info("REST API: Successfully retrieved {} card data records", cardDataList.size());
            return ResponseEntity.ok(cardDataList);
        } catch (Exception e) {
            logger.error("REST API: Error reading all card data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Read card data with pagination support
     * Implements paginated sequential access for batch processing
     */
    @GetMapping("/read-paginated")
    @Operation(
        summary = "Read card data with pagination",
        description = "Reads card data records with pagination support for efficient batch processing"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated card data records"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while reading card data")
    })
    public ResponseEntity<Page<CardDataDto>> readCardDataPaginated(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (1-1000)", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(1000) int size) {
        
        logger.info("REST API: Reading card data with pagination - page: {}, size: {}", page, size);
        
        try {
            Page<CardDataDto> cardDataPage = cardDataService.readCardDataWithPagination(page, size);
            logger.info("REST API: Successfully retrieved page {} with {} records", page, cardDataPage.getContent().size());
            return ResponseEntity.ok(cardDataPage);
        } catch (Exception e) {
            logger.error("REST API: Error reading card data with pagination: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Find card data by card number (primary key lookup)
     * Equivalent to indexed file access by FDCARDNUM
     */
    @GetMapping("/{cardNumber}")
    @Operation(
        summary = "Find card data by card number",
        description = "Retrieves card data by card number (FDCARDNUM), equivalent to indexed file access"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card data found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Card data not found for the given card number"),
        @ApiResponse(responseCode = "400", description = "Invalid card number format"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while retrieving card data")
    })
    public ResponseEntity<CardDataDto> getCardDataByNumber(
            @Parameter(description = "16-character card number", example = "1234567890123456")
            @PathVariable String cardNumber) {
        
        logger.info("REST API: Finding card data by card number: {}", cardNumber);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            logger.warn("REST API: Invalid card number format: {}", cardNumber);
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Optional<CardDataDto> cardData = cardDataService.findByCardNumber(cardNumber);
            if (cardData.isPresent()) {
                logger.info("REST API: Card data found for card number: {}", cardNumber);
                return ResponseEntity.ok(cardData.get());
            } else {
                logger.info("REST API: Card data not found for card number: {}", cardNumber);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("REST API: Error finding card data by card number {}: {}", cardNumber, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new card data record
     */
    @PostMapping
    @Operation(
        summary = "Create new card data record",
        description = "Creates a new card data record with FDCARDNUM and FDCARDDATA"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card data record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card data or card number already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating card data")
    })
    public ResponseEntity<CardDataDto> createCardData(
            @Parameter(description = "Card data to create")
            @Valid @RequestBody CardDataDto cardDataDto) {
        
        logger.info("REST API: Creating new card data record for card number: {}", cardDataDto.getCardNumber());
        
        try {
            CardDataDto createdCardData = cardDataService.createCardData(cardDataDto);
            logger.info("REST API: Successfully created card data record for card number: {}", createdCardData.getCardNumber());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCardData);
        } catch (IllegalArgumentException e) {
            logger.warn("REST API: Invalid card data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("REST API: Error creating card data record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update existing card data record
     */
    @PutMapping("/{cardNumber}")
    @Operation(
        summary = "Update card data record",
        description = "Updates an existing card data record by card number"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card data record updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card data"),
        @ApiResponse(responseCode = "404", description = "Card data not found for the given card number"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while updating card data")
    })
    public ResponseEntity<CardDataDto> updateCardData(
            @Parameter(description = "16-character card number", example = "1234567890123456")
            @PathVariable String cardNumber,
            @Parameter(description = "Updated card data")
            @Valid @RequestBody CardDataDto cardDataDto) {
        
        logger.info("REST API: Updating card data record for card number: {}", cardNumber);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            logger.warn("REST API: Invalid card number format: {}", cardNumber);
            return ResponseEntity.badRequest().build();
        }
        
        try {
            CardDataDto updatedCardData = cardDataService.updateCardData(cardNumber, cardDataDto);
            logger.info("REST API: Successfully updated card data record for card number: {}", cardNumber);
            return ResponseEntity.ok(updatedCardData);
        } catch (IllegalArgumentException e) {
            logger.warn("REST API: Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("REST API: Error updating card data record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete card data record
     */
    @DeleteMapping("/{cardNumber}")
    @Operation(
        summary = "Delete card data record",
        description = "Deletes a card data record by card number"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card data record deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card number format"),
        @ApiResponse(responseCode = "404", description = "Card data not found for the given card number"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while deleting card data")
    })
    public ResponseEntity<Void> deleteCardData(
            @Parameter(description = "16-character card number", example = "1234567890123456")
            @PathVariable String cardNumber) {
        
        logger.info("REST API: Deleting card data record for card number: {}", cardNumber);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            logger.warn("REST API: Invalid card number format: {}", cardNumber);
            return ResponseEntity.badRequest().build();
        }
        
        try {
            cardDataService.deleteCardData(cardNumber);
            logger.info("REST API: Successfully deleted card data record for card number: {}", cardNumber);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("REST API: Card data not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("REST API: Error deleting card data record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get total count of card data records
     */
    @GetMapping("/count")
    @Operation(
        summary = "Get total count of card data records",
        description = "Returns the total number of card data records in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved card data count"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while counting card data")
    })
    public ResponseEntity<Map<String, Long>> getCardDataCount() {
        logger.info("REST API: Getting total count of card data records");
        
        try {
            long count = cardDataService.getCardDataCount();
            logger.info("REST API: Total card data records count: {}", count);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            logger.error("REST API: Error getting card data count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search card data by content
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search card data by content",
        description = "Searches card data records by content within FDCARDDATA field"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching card data records"),
        @ApiResponse(responseCode = "400", description = "Invalid search term"),
        @ApiResponse(responseCode = "500", description = "Internal server error occurred while searching card data")
    })
    public ResponseEntity<List<CardDataDto>> searchCardData(
            @Parameter(description = "Search term to look for in card data", example = "search term")
            @RequestParam String searchTerm) {
        
        logger.info("REST API: Searching card data by content: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            logger.warn("REST API: Search term is null or empty");
            return ResponseEntity.badRequest().build();
        }
        
        try {
            List<CardDataDto> cardDataList = cardDataService.searchByCardData(searchTerm);
            logger.info("REST API: Found {} card data records matching search term", cardDataList.size());
            return ResponseEntity.ok(cardDataList);
        } catch (Exception e) {
            logger.error("REST API: Error searching card data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}