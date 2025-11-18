package com.example.demo.controller;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Credit Card Management", description = "APIs for managing credit cards with authorization and validation")
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Operation(summary = "Get all credit cards", description = "Retrieve a paginated list of all credit cards")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CreditCardResponseDto>> getAllCreditCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all credit cards with pagination: {}", pageable);
        Page<CreditCardResponseDto> cards = creditCardService.getAllCreditCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Get credit card by card number", 
               description = "Retrieve a credit card by its 16-digit card number (BR004: Card Record Retrieval)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit card"),
        @ApiResponse(responseCode = "400", description = "Invalid card number format - must be 16 digits"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> getCreditCardByNumber(@PathVariable String cardNumber) {
        log.info("Retrieving credit card by number");
        return creditCardService.getCreditCardByNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get credit cards by account ID", 
               description = "Retrieve all credit cards associated with a specific 11-digit account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID format"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<CreditCardResponseDto>> getCreditCardsByAccountId(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving credit cards for account ID: {}", accountId);
        Page<CreditCardResponseDto> cards = creditCardService.getCreditCardsByAccountId(accountId, pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Create a new credit card", 
               description = "Create a new credit card with validation of all fields")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credit card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - card number must be 16 digits, account ID must be 11 digits, card status must be Y or N"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CreditCardResponseDto> createCreditCard(@Valid @RequestBody CreateCreditCardRequestDto request) {
        log.info("Creating new credit card");
        CreditCardResponseDto response = creditCardService.createCreditCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing credit card", 
               description = "Update credit card details (BR002: Card Detail Modification - only embossed name, status, and expiration date can be modified). Includes optimistic locking for concurrent update prevention (BR003).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "409", description = "Concurrent update detected - card was modified by another user (BR003)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> updateCreditCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCreditCardRequestDto request,
            @RequestHeader(value = "X-User-ID", required = false) String userId) {
        log.info("Updating credit card by user: {}", userId);
        CreditCardResponseDto response = creditCardService.updateCreditCard(cardNumber, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a credit card", description = "Delete a credit card by its 16-digit card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credit card deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card number format"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable String cardNumber) {
        log.info("Deleting credit card");
        creditCardService.deleteCreditCard(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search credit cards", 
               description = "Search for credit cards using account ID and/or card number (BR001: Card Search and Retrieval, BR003: Search Criteria Requirement). At least one search criterion must be provided. User authorization is enforced (BR001: User Authorization for Card Viewing).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search - returns matching credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria - at least one criterion required, or invalid format"),
        @ApiResponse(responseCode = "403", description = "Access denied - user not authorized to view requested cards"),
        @ApiResponse(responseCode = "404", description = "No matching account/card combination found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CreditCardResponseDto>> searchCreditCards(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String cardNumber,
            @RequestHeader(value = "X-User-ID") Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching credit cards - Account ID: {}, User ID: {}", accountId, userId);
        Page<CreditCardResponseDto> results = creditCardService.searchCards(accountId, cardNumber, userId, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get card details with authorization", 
               description = "Retrieve specific card details by account ID and card number with user authorization check (BR001: Card Search and Retrieval, BR001: User Authorization for Card Viewing)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card details"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters - both account ID and card number required"),
        @ApiResponse(responseCode = "403", description = "Access denied - user not authorized to view this card"),
        @ApiResponse(responseCode = "404", description = "Account/card combination not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/details")
    public ResponseEntity<CreditCardResponseDto> getCardDetails(
            @RequestParam Long accountId,
            @RequestParam String cardNumber,
            @RequestHeader(value = "X-User-ID") Long userId) {
        log.info("Retrieving card details for account {} by user {}", accountId, userId);
        CreditCardResponseDto response = creditCardService.searchCardDetails(accountId, cardNumber, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return from list screen with previous criteria", 
               description = "Handle navigation when returning from the card list screen, maintaining previous search criteria (BR006: Return from List Screen)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval with previous criteria maintained"),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/return-from-list")
    public ResponseEntity<Page<CreditCardResponseDto>> returnFromListScreen(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String cardNumber,
            @RequestHeader(value = "X-User-ID") Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Returning from list screen with previous criteria - Account ID: {}", accountId);
        Page<CreditCardResponseDto> results = creditCardService.getCardsWithPreviousCriteria(
                accountId, cardNumber, userId, pageable);
        return ResponseEntity.ok(results);
    }
}
