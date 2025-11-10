package com.example.demo.controller;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Credit Card management.
 * Provides endpoints for credit card CRUD operations and filtering.
 * Implements business rules for card listing and filtering.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Credit Card Management", description = "APIs for managing credit cards")
@RequestMapping("/api/credit-cards")
public class CreditCardController {
    
    private final CreditCardService creditCardService;
    
    /**
     * Get all credit cards with pagination
     * BR001: Admin users can view all cards when no context is passed
     * BR010: When no records match, appropriate message is displayed
     * @param pageable pagination parameters
     * @return page of credit cards
     */
    @Operation(summary = "Get all credit cards", 
               description = "Retrieve a paginated list of all credit cards (Admin access)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CreditCardResponseDto>> getAllCreditCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards - Fetching all credit cards");
        Page<CreditCardResponseDto> cards = creditCardService.getAllCreditCards(pageable);
        
        // BR010: No records found handling
        if (cards.isEmpty()) {
            log.info("No credit cards found");
        }
        
        return ResponseEntity.ok(cards);
    }
    
    /**
     * Get credit card by card number
     * BR005: Single row selection enforcement
     * @param cardNumber the 16-digit card number
     * @return credit card details
     */
    @Operation(summary = "Get credit card by number", 
               description = "Retrieve a credit card by its 16-digit number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit card"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> getCreditCardByNumber(@PathVariable String cardNumber) {
        log.info("GET /api/credit-cards/{} - Fetching credit card", cardNumber);
        return creditCardService.getCreditCardByNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get credit cards by account ID
     * BR001: Non-admin users can only view cards associated with their specific account
     * BR003: Account ID must be a valid 11-digit numeric value
     * BR007: Records are filtered based on account ID criteria
     * @param accountId the account identifier (11 digits)
     * @param pageable pagination parameters
     * @return page of credit cards for the account
     */
    @Operation(summary = "Get credit cards by account", 
               description = "Retrieve credit cards for a specific account (11-digit ID)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByAccountId(
            @PathVariable String accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards/account/{} - Fetching credit cards by account", accountId);
        
        try {
            Page<CreditCardResponseDto> cards = creditCardService.getCreditCardsByAccountId(accountId, pageable);
            
            // BR010: No records found handling
            if (cards.isEmpty()) {
                log.info("No credit cards found for account: {}", accountId);
            }
            
            return ResponseEntity.ok(cards);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching credit cards: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get credit cards by status
     * @param cardStatus the status code (single character)
     * @param pageable pagination parameters
     * @return page of credit cards with the specified status
     */
    @Operation(summary = "Get credit cards by status", 
               description = "Retrieve credit cards filtered by status code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid status code"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{cardStatus}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByStatus(
            @PathVariable String cardStatus,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards/status/{} - Fetching credit cards by status", cardStatus);
        
        try {
            Page<CreditCardResponseDto> cards = creditCardService.getCreditCardsByStatus(cardStatus, pageable);
            return ResponseEntity.ok(cards);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching credit cards: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get credit cards by account ID and status
     * BR007: Both filters can be applied simultaneously
     * @param accountId the account identifier
     * @param cardStatus the status code
     * @param pageable pagination parameters
     * @return page of credit cards matching both criteria
     */
    @Operation(summary = "Get credit cards by account and status", 
               description = "Retrieve credit cards filtered by both account ID and status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}/status/{cardStatus}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByAccountIdAndStatus(
            @PathVariable String accountId,
            @PathVariable String cardStatus,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards/account/{}/status/{} - Fetching credit cards", accountId, cardStatus);
        
        try {
            Page<CreditCardResponseDto> cards = creditCardService.getCreditCardsByAccountIdAndStatus(
                    accountId, cardStatus, pageable);
            return ResponseEntity.ok(cards);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching credit cards: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search credit cards by card number pattern
     * BR007: Records are filtered based on card number criteria when specified
     * @param cardNumberPattern the card number pattern to search
     * @param pageable pagination parameters
     * @return page of credit cards matching the pattern
     */
    @Operation(summary = "Search credit cards by number", 
               description = "Search credit cards by card number pattern")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CreditCardResponseDto>> searchCreditCardsByNumber(
            @RequestParam String cardNumberPattern,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards/search?cardNumberPattern={} - Searching credit cards", cardNumberPattern);
        Page<CreditCardResponseDto> cards = creditCardService.searchCreditCardsByNumber(cardNumberPattern, pageable);
        return ResponseEntity.ok(cards);
    }
    
    /**
     * Search credit cards by account ID and card number pattern
     * BR007: Both filters can be applied simultaneously
     * @param accountId the account identifier
     * @param cardNumberPattern the card number pattern
     * @param pageable pagination parameters
     * @return page of credit cards matching both criteria
     */
    @Operation(summary = "Search credit cards by account and number", 
               description = "Search credit cards by account ID and card number pattern")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}/search")
    public ResponseEntity<Page<CreditCardResponseDto>> searchCreditCardsByAccountIdAndNumber(
            @PathVariable String accountId,
            @RequestParam String cardNumberPattern,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/credit-cards/account/{}/search?cardNumberPattern={} - Searching credit cards", 
                accountId, cardNumberPattern);
        
        try {
            Page<CreditCardResponseDto> cards = creditCardService.searchCreditCardsByAccountIdAndNumber(
                    accountId, cardNumberPattern, pageable);
            return ResponseEntity.ok(cards);
        } catch (IllegalArgumentException e) {
            log.error("Error searching credit cards: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Create a new credit card
     * Validates card number (16 digits) and account ID (11 digits)
     * @param request the create credit card request
     * @return created credit card details
     */
    @Operation(summary = "Create a new credit card", 
               description = "Create a new credit card with 16-digit number and 11-digit account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credit card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or card already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CreditCardResponseDto> createCreditCard(
            @Valid @RequestBody CreateCreditCardRequestDto request) {
        log.info("POST /api/credit-cards - Creating new credit card");
        
        try {
            CreditCardResponseDto response = creditCardService.createCreditCard(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating credit card: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update an existing credit card
     * @param cardNumber the card number
     * @param request the update credit card request
     * @return updated credit card details
     */
    @Operation(summary = "Update an existing credit card", 
               description = "Update credit card details by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or card cannot be modified"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> updateCreditCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCreditCardRequestDto request) {
        log.info("PUT /api/credit-cards/{} - Updating credit card", cardNumber);
        
        try {
            CreditCardResponseDto response = creditCardService.updateCreditCard(cardNumber, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating credit card: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete a credit card
     * @param cardNumber the card number
     * @return no content
     */
    @Operation(summary = "Delete a credit card", 
               description = "Delete a credit card by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credit card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable String cardNumber) {
        log.info("DELETE /api/credit-cards/{} - Deleting credit card", cardNumber);
        
        try {
            creditCardService.deleteCreditCard(cardNumber);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting credit card: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get count of credit cards by account ID
     * @param accountId the account identifier
     * @return count of credit cards
     */
    @Operation(summary = "Count credit cards by account", 
               description = "Get the count of credit cards for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}/count")
    public ResponseEntity<Long> countCreditCardsByAccountId(@PathVariable String accountId) {
        log.info("GET /api/credit-cards/account/{}/count - Counting credit cards", accountId);
        long count = creditCardService.countCreditCardsByAccountId(accountId);
        return ResponseEntity.ok(count);
    }
}
