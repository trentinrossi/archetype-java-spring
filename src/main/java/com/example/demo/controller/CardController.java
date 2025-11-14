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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Management", description = "APIs for managing credit cards and bill payment processing")
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
        log.info("Retrieving all cards with pagination: {}", pageable);
        Page<CardResponseDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Get card by ID", description = "Retrieve a card by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getCardById(@PathVariable Long id) {
        log.info("Retrieving card by ID: {}", id);
        return cardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get card by card number", description = "Retrieve a card by its card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-card-number/{cardNumber}")
    public ResponseEntity<CardResponseDto> getCardByCardNumber(@PathVariable String cardNumber) {
        log.info("Retrieving card by card number: {}", cardNumber);
        return cardService.getCardByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get cards by account ID", description = "Retrieve all cards associated with an account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "404", description = "No cards found for account"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<CardResponseDto>> getCardsByAccountId(@PathVariable String accountId) {
        log.info("Retrieving cards by account ID: {}", accountId);
        List<CardResponseDto> cards = cardService.getCardsByAccountId(accountId);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Create a new card", description = "Create a new credit card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@RequestBody CreateCardRequestDto request) {
        log.info("Creating new card for account: {}", request.getXrefAcctId());
        CardResponseDto response = cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing card", description = "Update card details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long id,
                                                      @RequestBody UpdateCardRequestDto request) {
        log.info("Updating card with ID: {}", id);
        CardResponseDto response = cardService.updateCard(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a card", description = "Delete a card by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.info("Deleting card with ID: {}", id);
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validate card and account relationship", description = "Validate that a card number is associated with the correct account ID for bill payment processing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed"),
        @ApiResponse(responseCode = "404", description = "Card or account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCard(@RequestBody Map<String, String> request) {
        String cardNumber = request.get("xrefCardNum");
        String accountId = request.get("xrefAcctId");
        
        log.info("Validating card number: {} for account: {}", cardNumber, accountId);
        
        boolean isValid = cardService.validateCardAndAccountRelationship(cardNumber, accountId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("xrefCardNum", cardNumber);
        response.put("xrefAcctId", accountId);
        response.put("valid", isValid);
        response.put("message", isValid ? "Card and account relationship is valid" : "Card and account relationship is invalid");
        
        return ResponseEntity.ok(response);
    }
}
