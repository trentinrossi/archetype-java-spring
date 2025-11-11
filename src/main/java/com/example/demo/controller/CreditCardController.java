package com.example.demo.controller;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.CreditCardFilterRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * CreditCardController
 * 
 * REST API controller for Credit Card management (COCRDLIC Program).
 * Provides endpoints for CRUD operations, filtering, and navigation.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR002: Card Number Filter Validation
 * - BR003: Single Selection Enforcement
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR006: Filter Record Matching
 * - BR008: First Page Navigation Restriction
 * - BR009: Last Page Navigation Restriction
 * - BR011: Exit to Menu
 * - BR012: View Card Details
 * - BR013: Update Card Information
 * - BR014: Forward Pagination
 * - BR015: Backward Pagination
 * - BR017: Input Error Protection
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Credit Card Management", description = "APIs for managing credit cards (COCRDLIC Program)")
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Operation(summary = "Get all credit cards", 
               description = "Retrieve a paginated list of all credit cards. Implements BR014, BR015 - Pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CreditCardResponseDto>> getAllCreditCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching all credit cards with pagination");
        Page<CreditCardResponseDto> creditCards = creditCardService.getAllCreditCards(pageable);
        return ResponseEntity.ok(creditCards);
    }

    @Operation(summary = "Get credit card by card number", 
               description = "Retrieve a specific credit card by its 16-digit card number. Implements BR002 - Card Number Filter Validation.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of credit card"),
        @ApiResponse(responseCode = "400", description = "Invalid card number - must be 16 digits"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> getCreditCardByCardNumber(
            @Parameter(description = "Card number (16 digit numeric value)", required = true)
            @PathVariable String cardNumber) {
        log.info("Fetching credit card with card number: {}", cardNumber);
        return creditCardService.getCreditCardByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new credit card", 
               description = "Create a new credit card. Implements BR002, BR004, BR005 - Field Validations.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credit card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - validation errors"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CreditCardResponseDto> createCreditCard(
            @Valid @RequestBody CreateCreditCardRequestDto request) {
        log.info("Creating new credit card");
        CreditCardResponseDto response = creditCardService.createCreditCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing credit card", 
               description = "Update credit card details. Implements BR013 - Update Card Information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CreditCardResponseDto> updateCreditCard(
            @Parameter(description = "Card number (16 digit numeric value)", required = true)
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCreditCardRequestDto request) {
        log.info("Updating credit card with card number: {}", cardNumber);
        CreditCardResponseDto response = creditCardService.updateCreditCard(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a credit card", description = "Delete a credit card by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credit card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Credit card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCreditCard(
            @Parameter(description = "Card number (16 digit numeric value)", required = true)
            @PathVariable String cardNumber) {
        log.info("Deleting credit card with card number: {}", cardNumber);
        creditCardService.deleteCreditCard(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filter credit cards", 
               description = "Filter credit cards based on user permissions and filter criteria. Implements BR001 (User Permission Based Card Access), BR006 (Filter Record Matching), BR014, BR015 (Pagination). Admin users can view all cards, regular users only see cards for their accessible accounts.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of filtered credit cards"),
        @ApiResponse(responseCode = "400", description = "Invalid filter parameters - card number must be 16 digits, account ID must be 11 digits"),
        @ApiResponse(responseCode = "403", description = "Access denied - user has no accessible accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public ResponseEntity<Page<CreditCardResponseDto>> filterCreditCards(
            @Parameter(description = "User ID requesting the cards", required = true)
            @RequestParam String userId,
            @Valid @RequestBody CreditCardFilterRequestDto filter,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Filtering credit cards for user: {} with filter: {}", userId, filter);
        Page<CreditCardResponseDto> creditCards = creditCardService.filterCreditCards(userId, filter, pageable);
        return ResponseEntity.ok(creditCards);
    }

    @Operation(summary = "Validate single selection", 
               description = "Validate that only one credit card is selected for view or update operations. Implements BR003 - Single Selection Enforcement.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Selection is valid"),
        @ApiResponse(responseCode = "400", description = "Invalid selection - only one card can be selected at a time"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate-selection")
    public ResponseEntity<Void> validateSingleSelection(
            @Parameter(description = "Number of cards selected", required = true)
            @RequestParam int selectionCount) {
        log.info("Validating single selection: count={}", selectionCount);
        creditCardService.validateSingleSelection(selectionCount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate backward navigation", 
               description = "Validate navigation to previous page. Implements BR008 - First Page Navigation Restriction.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Navigation is valid"),
        @ApiResponse(responseCode = "400", description = "Cannot navigate backward - NO PREVIOUS PAGES TO DISPLAY"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate-backward-navigation")
    public ResponseEntity<Void> validateBackwardNavigation(Pageable pageable) {
        log.info("Validating backward navigation: currentPage={}", pageable.getPageNumber());
        creditCardService.validateBackwardNavigation(pageable);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate forward navigation", 
               description = "Validate navigation to next page. Implements BR009 - Last Page Navigation Restriction.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Navigation is valid"),
        @ApiResponse(responseCode = "400", description = "Cannot navigate forward - NO MORE PAGES TO DISPLAY"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate-forward-navigation")
    public ResponseEntity<Void> validateForwardNavigation(
            @Parameter(description = "Current page information", required = true)
            @RequestBody Page<?> page) {
        log.info("Validating forward navigation: isLast={}", page.isLast());
        creditCardService.validateForwardNavigation(page);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Check row selection protection", 
               description = "Check if row selection should be protected due to validation errors. Implements BR017 - Input Error Protection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Protection status returned"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/check-row-protection")
    public ResponseEntity<Boolean> checkRowSelectionProtection(
            @Parameter(description = "Whether validation errors exist", required = true)
            @RequestParam boolean hasValidationErrors) {
        log.info("Checking row selection protection: hasValidationErrors={}", hasValidationErrors);
        boolean isProtected = creditCardService.shouldProtectRowSelection(hasValidationErrors);
        return ResponseEntity.ok(isProtected);
    }

    @Operation(summary = "Get view details target program", 
               description = "Get the target program for viewing card details. Implements BR012 - View Card Details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Target program returned"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/view-details-target")
    public ResponseEntity<String> getViewDetailsTargetProgram() {
        log.info("Getting view details target program");
        String targetProgram = creditCardService.getViewDetailsTargetProgram();
        return ResponseEntity.ok(targetProgram);
    }

    @Operation(summary = "Get update card target program", 
               description = "Get the target program for updating card information. Implements BR013 - Update Card Information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Target program returned"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/update-card-target")
    public ResponseEntity<String> getUpdateCardTargetProgram() {
        log.info("Getting update card target program");
        String targetProgram = creditCardService.getUpdateCardTargetProgram();
        return ResponseEntity.ok(targetProgram);
    }

    @Operation(summary = "Get exit menu target program", 
               description = "Get the target program for exiting to main menu. Implements BR011 - Exit to Menu.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Target program returned"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/exit-menu-target")
    public ResponseEntity<String> getExitMenuTargetProgram() {
        log.info("Getting exit menu target program");
        String targetProgram = creditCardService.getExitMenuTargetProgram();
        return ResponseEntity.ok(targetProgram);
    }
}
