package com.example.demo.controller;

import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.enums.CardStatus;
import com.example.demo.service.CardCrossReferenceService;
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

import java.util.List;

/**
 * REST Controller for CardCrossReference operations
 * Provides endpoints for card cross reference management
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Cross Reference Management", description = "APIs for managing card cross references")
@RequestMapping("/api/cards")
public class CardCrossReferenceController {
    
    private final CardCrossReferenceService cardCrossReferenceService;
    
    @Operation(summary = "Get all card cross references", description = "Retrieve a paginated list of all card cross references")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of cards"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getAllCardCrossReferences(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/cards - Fetching all card cross references");
        Page<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getAllCardCrossReferences(pageable);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get card cross reference by card number", description = "Retrieve a card cross reference by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(
            @PathVariable String cardNumber) {
        log.info("GET /api/cards/{} - Fetching card cross reference", cardNumber);
        return cardCrossReferenceService.getCardCrossReferenceByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new card cross reference", description = "Create a new card cross reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardCrossReferenceResponseDto> createCardCrossReference(
            @RequestBody CreateCardCrossReferenceRequestDto request) {
        log.info("POST /api/cards - Creating new card cross reference");
        CardCrossReferenceResponseDto response = cardCrossReferenceService.createCardCrossReference(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing card cross reference", description = "Update card cross reference details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardCrossReferenceResponseDto> updateCardCrossReference(
            @PathVariable String cardNumber,
            @RequestBody UpdateCardCrossReferenceRequestDto request) {
        log.info("PUT /api/cards/{} - Updating card cross reference", cardNumber);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.updateCardCrossReference(cardNumber, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a card cross reference", description = "Delete a card cross reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCardCrossReference(@PathVariable String cardNumber) {
        log.info("DELETE /api/cards/{} - Deleting card cross reference", cardNumber);
        cardCrossReferenceService.deleteCardCrossReference(cardNumber);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get card cross references by customer ID", 
               description = "Retrieve all card cross references for a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByCustomerId(
            @PathVariable String customerId) {
        log.info("GET /api/cards/customer/{} - Fetching cards for customer", customerId);
        List<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getCardCrossReferencesByCustomerId(customerId);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get card cross references by account ID", 
               description = "Retrieve all card cross references for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByAccountId(
            @PathVariable String accountId) {
        log.info("GET /api/cards/account/{} - Fetching cards for account", accountId);
        List<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getCardCrossReferencesByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get card cross references by status", description = "Retrieve card cross references by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getCardCrossReferencesByStatus(
            @PathVariable CardStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/cards/status/{} - Fetching cards by status", status);
        Page<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getCardCrossReferencesByStatus(status, pageable);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get active cards", description = "Retrieve all active cards")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getActiveCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/cards/active - Fetching active cards");
        Page<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getActiveCards(pageable);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get expired cards", description = "Retrieve all expired cards")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expired")
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getExpiredCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/cards/expired - Fetching expired cards");
        Page<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getExpiredCards(pageable);
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Get cards expiring soon", description = "Retrieve cards expiring within 3 months")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardsExpiringSoon() {
        log.info("GET /api/cards/expiring-soon - Fetching cards expiring soon");
        List<CardCrossReferenceResponseDto> cards = cardCrossReferenceService.getCardsExpiringSoon();
        return ResponseEntity.ok(cards);
    }
    
    @Operation(summary = "Activate a card", description = "Activate a card that is in pending activation status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card activated successfully"),
        @ApiResponse(responseCode = "400", description = "Card cannot be activated"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/activate")
    public ResponseEntity<CardCrossReferenceResponseDto> activateCard(@PathVariable String cardNumber) {
        log.info("POST /api/cards/{}/activate - Activating card", cardNumber);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.activateCard(cardNumber);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Report card as lost", description = "Report a card as lost")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card reported as lost successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/report-lost")
    public ResponseEntity<CardCrossReferenceResponseDto> reportCardLost(@PathVariable String cardNumber) {
        log.info("POST /api/cards/{}/report-lost - Reporting card as lost", cardNumber);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.reportCardLostOrStolen(cardNumber, false);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Report card as stolen", description = "Report a card as stolen")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card reported as stolen successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/report-stolen")
    public ResponseEntity<CardCrossReferenceResponseDto> reportCardStolen(@PathVariable String cardNumber) {
        log.info("POST /api/cards/{}/report-stolen - Reporting card as stolen", cardNumber);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.reportCardLostOrStolen(cardNumber, true);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Set PIN for card", description = "Set PIN for a card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PIN set successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/set-pin")
    public ResponseEntity<CardCrossReferenceResponseDto> setPinForCard(@PathVariable String cardNumber) {
        log.info("POST /api/cards/{}/set-pin - Setting PIN for card", cardNumber);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.setPinForCard(cardNumber);
        return ResponseEntity.ok(response);
    }
}
