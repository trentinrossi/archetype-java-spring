package com.example.demo.controller;

import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.service.CardCrossReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Cross Reference Management", description = "APIs for managing card-to-account mappings")
@RequestMapping("/api/card-cross-references")
public class CardCrossReferenceController {

    private final CardCrossReferenceService cardCrossReferenceService;

    @Operation(summary = "Create card-to-account mapping", description = "Create a new card-to-account mapping")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card cross reference created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardCrossReferenceResponseDto> createCardCrossReference(
            @Valid @RequestBody CreateCardCrossReferenceRequestDto request) {
        log.info("Creating card cross reference for card number: {}", request.getCardNumber());
        CardCrossReferenceResponseDto response = cardCrossReferenceService.createCardCrossReference(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get mapping by card number", description = "Retrieve card-to-account mapping by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross reference"),
        @ApiResponse(responseCode = "404", description = "Card cross reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(
            @PathVariable String cardNumber) {
        log.info("Retrieving card cross reference for card number: {}", cardNumber);
        return cardCrossReferenceService.getCardCrossReferenceByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get mappings by account ID", description = "Retrieve all card-to-account mappings for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross references"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByAccountId(
            @PathVariable Long accountId) {
        log.info("Retrieving card cross references for account ID: {}", accountId);
        List<CardCrossReferenceResponseDto> responses = cardCrossReferenceService.getCardCrossReferencesByAccountId(accountId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Delete mapping", description = "Delete a card-to-account mapping by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross reference deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card cross reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCardCrossReference(@PathVariable String cardNumber) {
        log.info("Deleting card cross reference with card number: {}", cardNumber);
        cardCrossReferenceService.deleteCardCrossReference(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
