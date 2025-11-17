package com.example.demo.controller;

import com.example.demo.dto.CardResponseDto;
import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.service.CardService;
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
@Tag(name = "Card Management", description = "APIs for managing credit cards")
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Get all cards", description = "Retrieve a paginated list of all cards")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getAllCards(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CardResponseDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Get card by number", description = "Retrieve a card by their number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNum}")
    public ResponseEntity<CardResponseDto> getCardByNumber(@PathVariable String cardNum) {
        return cardService.getCardByNumber(cardNum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new card", description = "Create a new card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @Valid @RequestBody CreateCardRequestDto request) {
        CardResponseDto response = cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing card", description = "Update card details by number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNum}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable String cardNum,
            @Valid @RequestBody UpdateCardRequestDto request) {
        CardResponseDto response = cardService.updateCard(cardNum, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a card", description = "Delete a card by number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNum}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNum) {
        cardService.deleteCard(cardNum);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get cards by account ID", description = "BR014: Retrieve all cards for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{acctId}")
    public ResponseEntity<Page<CardResponseDto>> getCardsByAccountId(
            @PathVariable Long acctId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CardResponseDto> cards = cardService.getCardsByAccountId(acctId, pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Validate card exists", description = "BR003: Validate that card number exists in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card exists"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate/{cardNum}")
    public ResponseEntity<Void> validateCardExists(@PathVariable String cardNum) {
        cardService.validateCardExists(cardNum);
        return ResponseEntity.ok().build();
    }
}
