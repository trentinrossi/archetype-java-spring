package com.carddemo.billpayment.controller;

import com.carddemo.billpayment.dto.CardCrossReferenceResponseDto;
import com.carddemo.billpayment.dto.CreateCardCrossReferenceRequestDto;
import com.carddemo.billpayment.dto.UpdateCardCrossReferenceRequestDto;
import com.carddemo.billpayment.service.CardCrossReferenceService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Cross Reference Management", description = "APIs for managing card-account cross-references")
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

    @Operation(summary = "Get card cross-reference by ID", description = "Retrieve a card cross-reference by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-reference"),
        @ApiResponse(responseCode = "404", description = "Card cross-reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardCrossReferenceResponseDto> getCardCrossReferenceById(@PathVariable Long id) {
        log.info("Retrieving card cross-reference by ID: {}", id);
        return cardCrossReferenceService.getCardCrossReferenceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get card cross-references by account ID", description = "Retrieve all card cross-references for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-references"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{acctId}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByAccountId(
            @PathVariable String acctId) {
        log.info("Retrieving card cross-references by account ID: {}", acctId);
        List<CardCrossReferenceResponseDto> crossReferences = 
                cardCrossReferenceService.getCardCrossReferencesByAccountId(acctId);
        return ResponseEntity.ok(crossReferences);
    }

    @Operation(summary = "Get card cross-references by card number", description = "Retrieve all card cross-references for a specific card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross-references"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNum}")
    public ResponseEntity<List<CardCrossReferenceResponseDto>> getCardCrossReferencesByCardNum(
            @PathVariable String cardNum) {
        log.info("Retrieving card cross-references by card number");
        List<CardCrossReferenceResponseDto> crossReferences = 
                cardCrossReferenceService.getCardCrossReferencesByCardNum(cardNum);
        return ResponseEntity.ok(crossReferences);
    }

    @Operation(summary = "Create a new card cross-reference", description = "Create a new card-account cross-reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card cross-reference created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardCrossReferenceResponseDto> createCardCrossReference(
            @Valid @RequestBody CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross-reference");
        CardCrossReferenceResponseDto response = cardCrossReferenceService.createCardCrossReference(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing card cross-reference", description = "Update card cross-reference details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card cross-reference updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card cross-reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardCrossReferenceResponseDto> updateCardCrossReference(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross-reference with ID: {}", id);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.updateCardCrossReference(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a card cross-reference", description = "Delete a card cross-reference by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross-reference deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card cross-reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardCrossReference(@PathVariable Long id) {
        log.info("Deleting card cross-reference with ID: {}", id);
        cardCrossReferenceService.deleteCardCrossReference(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Validate card for account", description = "Validate that a card is associated with an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCardForAccount(
            @RequestParam String acctId, 
            @RequestParam String cardNum) {
        log.info("Validating card for account: {}", acctId);
        
        boolean isValid = cardCrossReferenceService.validateCardForAccount(acctId, cardNum);
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        response.put("accountId", acctId);
        response.put("message", isValid ? "Card is associated with account" : "Card is not associated with account");
        
        return ResponseEntity.ok(response);
    }
}
