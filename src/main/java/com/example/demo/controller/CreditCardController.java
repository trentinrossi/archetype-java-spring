package com.example.demo.controller;

import com.example.demo.dto.*;
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
 * REST Controller for Credit Card management
 * Business Rule BR001: User Permission Based Card Listing
 * Business Rule BR002: Pagination Display Limit (max 7 records per page)
 * Business Rule BR003: Single Selection Enforcement
 * Business Rule BR004: Filter Application Logic
 * Business Rule BR005: Page Navigation State Management
 * Business Rule BR006: Program Integration Flow
 * Business Rule BR008: Record Exclusion Based on Filters
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Credit Card Management", description = "APIs for managing credit cards with user permissions and filtering")
@RequestMapping("/api/credit-cards")
public class CreditCardController {
    
    private final CreditCardService creditCardService;
    
    /**
     * Get all credit cards with pagination
     * Business Rule BR002: Maximum of 7 records per page
     */
    @Operation(summary = "Get all credit cards", 
               description = "Retrieve a paginated list of all credit cards (max 7 per page)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CreditCardResponseDto>> getAllCreditCards(
            @PageableDefault(size = 7) Pageable pageable) {
        log.info("GET /api/credit-cards - Fetching all credit cards");
        Page<CreditCardResponseDto> creditCards = creditCardService.getAllCreditCards(pageable);
        return ResponseEntity.ok(creditCards);
    }
    
    /**
     * Get credit card by ID
     * Business Rule BR003: Single Selection Enforcement
     */
    @Operation(summary = "Get credit card by ID", 
               description = "Retrieve a single credit card by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit card"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDto> getCreditCardById(@PathVariable Long id) {
        log.info("GET /api/credit-cards/{} - Fetching credit card", id);
        return creditCardService.getCreditCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get credit card by card number
     */
    @Operation(summary = "Get credit card by card number", 
               description = "Retrieve a credit card by its 16-digit card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit card"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card-number/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> getCreditCardByCardNumber(@PathVariable String cardNumber) {
        log.info("GET /api/credit-cards/card-number/{} - Fetching credit card", cardNumber);
        return creditCardService.getCreditCardByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get credit cards with filters and user permissions
     * Business Rule BR001: User Permission Based Card Listing
     * Business Rule BR004: Filter Application Logic
     * Business Rule BR005: Page Navigation State Management
     * Business Rule BR008: Record Exclusion Based on Filters
     */
    @Operation(summary = "Get credit cards with filters", 
               description = "Retrieve credit cards filtered by account ID and/or card number with user permission checks. " +
                           "Admin users can view all cards. Non-admin users can only view cards from their accessible accounts. " +
                           "Maximum 7 records per page.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "User does not have access to specified account"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public ResponseEntity<?> getCreditCardsWithFilters(
            @RequestParam String userId,
            @RequestBody CreditCardFilterRequestDto filter,
            @PageableDefault(size = 7) Pageable pageable) {
        log.info("POST /api/credit-cards/filter - User: {}, AccountId: {}, CardNumber: {}", 
                userId, filter.getAccountId(), filter.getCardNumber());
        try {
            CreditCardPageResponseDto response = creditCardService.getCreditCardsWithFilters(
                    userId, filter, pageable);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching credit cards: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    /**
     * Get credit cards by account ID
     * Business Rule BR004: Filter by account ID
     */
    @Operation(summary = "Get credit cards by account ID", 
               description = "Retrieve all credit cards for a specific account (max 7 per page)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByAccountId(
            @PathVariable String accountId,
            @PageableDefault(size = 7) Pageable pageable) {
        log.info("GET /api/credit-cards/account/{} - Fetching credit cards", accountId);
        Page<CreditCardResponseDto> creditCards = creditCardService.getCreditCardsByAccountId(
                accountId, pageable);
        return ResponseEntity.ok(creditCards);
    }
    
    /**
     * Get credit cards accessible by user
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    @Operation(summary = "Get credit cards by user ID", 
               description = "Retrieve credit cards accessible by a specific user (max 7 per page)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByUserId(
            @PathVariable String userId,
            @PageableDefault(size = 7) Pageable pageable) {
        log.info("GET /api/credit-cards/user/{} - Fetching credit cards", userId);
        Page<CreditCardResponseDto> creditCards = creditCardService.getCreditCardsByUserId(
                userId, pageable);
        return ResponseEntity.ok(creditCards);
    }
    
    /**
     * Create a new credit card
     */
    @Operation(summary = "Create a new credit card", 
               description = "Create a new credit card with 16-digit card number and associate with an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credit card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - Card number must be 16 digits, Account ID must be 11 digits"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "409", description = "Credit card already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CreditCardResponseDto> createCreditCard(
            @Valid @RequestBody CreateCreditCardRequestDto request) {
        log.info("POST /api/credit-cards - Creating new credit card for account: {}", 
                request.getAccountId());
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
     * Business Rule BR003: Single Selection Enforcement (one card at a time)
     * Business Rule BR006: Program Integration Flow (for card update operations)
     */
    @Operation(summary = "Update an existing credit card", 
               description = "Update credit card details (primarily status) by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CreditCardResponseDto> updateCreditCard(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCreditCardRequestDto request) {
        log.info("PUT /api/credit-cards/{} - Updating credit card", id);
        try {
            CreditCardResponseDto response = creditCardService.updateCreditCard(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating credit card: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete a credit card
     */
    @Operation(summary = "Delete a credit card", description = "Delete a credit card by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credit card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        log.info("DELETE /api/credit-cards/{} - Deleting credit card", id);
        try {
            creditCardService.deleteCreditCard(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting credit card: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
