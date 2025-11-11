package com.example.demo.controller;

import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.service.CardCrossReferenceService;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Cross Reference Management", description = "APIs for managing card-to-account cross-references")
@RequestMapping("/api/card-cross-references")
public class CardCrossReferenceController {

    private final CardCrossReferenceService cardCrossReferenceService;

    @Operation(summary = "Get all card cross-references", description = "Retrieve a paginated list of all card cross-references")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-references"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getAllCardCrossReferences(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all card cross-references with pagination: {}", pageable);
        Page<CardCrossReferenceResponseDto> crossReferences = cardCrossReferenceService.getAllCardCrossReferences(pageable);
        return ResponseEntity.ok(crossReferences);
    }

    @Operation(summary = "Get card cross-reference by account ID and card number", 
               description = "Retrieve a specific card cross-reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-reference"),
        @ApiResponse(responseCode = "404", description = "Card cross-reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}/{cardNumber}")
    public ResponseEntity<CardCrossReferenceResponseDto> getCardCrossReference(
            @PathVariable String accountId,
            @PathVariable String cardNumber) {
        log.info("Retrieving card cross-reference for account ID: {} and card number: {}", accountId, cardNumber);
        
        return cardCrossReferenceService.getCardCrossReference(accountId, cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get card cross-references by account ID", 
               description = "Retrieve all card cross-references for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-references"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByAccountId(
            @PathVariable String accountId) {
        log.info("Retrieving card cross-references for account ID: {}", accountId);
        List<CardCrossReferenceResponseDto> crossReferences = 
                cardCrossReferenceService.getCardCrossReferencesByAccountId(accountId);
        return ResponseEntity.ok(crossReferences);
    }

    @Operation(summary = "Get card cross-references by card number", 
               description = "Retrieve all card cross-references for a specific card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-references"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByCardNumber(
            @PathVariable String cardNumber) {
        log.info("Retrieving card cross-references for card number: {}", cardNumber);
        List<CardCrossReferenceResponseDto> crossReferences = 
                cardCrossReferenceService.getCardCrossReferencesByCardNumber(cardNumber);
        return ResponseEntity.ok(crossReferences);
    }

    @Operation(summary = "Create a new card cross-reference", 
               description = "Create a new card-to-account cross-reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card cross-reference created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "409", description = "Card cross-reference already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardCrossReferenceResponseDto> createCardCrossReference(
            @Valid @RequestBody CreateCardCrossReferenceRequestDto request) {
        log.info("Creating card cross-reference for account ID: {} and card number: {}", 
                request.getAccountId(), request.getCardNumber());
        
        try {
            CardCrossReferenceResponseDto response = cardCrossReferenceService.createCardCrossReference(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a card cross-reference", 
               description = "Delete a card cross-reference by account ID and card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross-reference deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card cross-reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{accountId}/{cardNumber}")
    public ResponseEntity<Void> deleteCardCrossReference(
            @PathVariable String accountId,
            @PathVariable String cardNumber) {
        log.info("Deleting card cross-reference for account ID: {} and card number: {}", accountId, cardNumber);
        
        cardCrossReferenceService.deleteCardCrossReference(accountId, cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all card cross-references for an account", 
               description = "Delete all card cross-references associated with a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross-references deleted successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<Void> deleteCardCrossReferencesByAccountId(@PathVariable String accountId) {
        log.info("Deleting all card cross-references for account ID: {}", accountId);
        
        cardCrossReferenceService.deleteCardCrossReferencesByAccountId(accountId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all card cross-references for a card", 
               description = "Delete all card cross-references associated with a specific card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross-references deleted successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/card/{cardNumber}")
    public ResponseEntity<Void> deleteCardCrossReferencesByCardNumber(@PathVariable String cardNumber) {
        log.info("Deleting all card cross-references for card number: {}", cardNumber);
        
        cardCrossReferenceService.deleteCardCrossReferencesByCardNumber(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if card cross-reference exists", 
               description = "Check if a card cross-reference exists for account and card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/exists/{accountId}/{cardNumber}")
    public ResponseEntity<Boolean> existsCardCrossReference(
            @PathVariable String accountId,
            @PathVariable String cardNumber) {
        log.info("Checking if card cross-reference exists for account ID: {} and card number: {}", 
                accountId, cardNumber);
        
        boolean exists = cardCrossReferenceService.existsCardCrossReference(accountId, cardNumber);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Count card cross-references by account", 
               description = "Count the number of card cross-references for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/account/{accountId}")
    public ResponseEntity<Long> countByAccountId(@PathVariable String accountId) {
        log.info("Counting card cross-references for account ID: {}", accountId);
        
        long count = cardCrossReferenceService.countByAccountId(accountId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Count card cross-references by card", 
               description = "Count the number of card cross-references for a specific card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/card/{cardNumber}")
    public ResponseEntity<Long> countByCardNumber(@PathVariable String cardNumber) {
        log.info("Counting card cross-references for card number: {}", cardNumber);
        
        long count = cardCrossReferenceService.countByCardNumber(cardNumber);
        return ResponseEntity.ok(count);
    }
}
