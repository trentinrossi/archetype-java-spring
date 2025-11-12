package com.example.demo.controller;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.service.CardService;
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
@Tag(name = "Card Management", description = "APIs for managing credit cards and card data operations")
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Get all cards", description = "Retrieve a paginated list of all cards")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getAllCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        Page<CardResponseDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Get card by card number", description = "Retrieve a card by its 16-digit card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardResponseDto> getCardByCardNumber(@PathVariable String cardNumber) {
        log.info("Retrieving card by card number");
        return cardService.getCardByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new card", description = "Create a new credit card with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CreateCardRequestDto request) {
        log.info("Creating new card");
        CardResponseDto response = cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing card", description = "Update card details by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card updated successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCardRequestDto request) {
        log.info("Updating card");
        CardResponseDto response = cardService.updateCard(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a card", description = "Delete a card by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        log.info("Deleting card");
        cardService.deleteCard(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get cards by account ID", description = "Retrieve all cards associated with a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardResponseDto>> getCardsByAccountId(@PathVariable Long accountId) {
        log.info("Retrieving cards for account: {}", accountId);
        List<CardResponseDto> cards = cardService.getCardsByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Get cards by customer ID", description = "Retrieve all cards associated with a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CardResponseDto>> getCardsByCustomerId(@PathVariable Long customerId) {
        log.info("Retrieving cards for customer: {}", customerId);
        List<CardResponseDto> cards = cardService.getCardsByCustomerId(customerId);
        return ResponseEntity.ok(cards);
    }
}
