package com.example.demo.controller;

import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.dto.CardCrossReferenceResponseDto;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Card Cross Reference Management", description = "APIs for managing card cross references")
@RequestMapping("/api/card-cross-references")
public class CardCrossReferenceController {

    private final CardCrossReferenceService cardCrossReferenceService;

    @Operation(summary = "Get all card cross references", description = "Retrieve a paginated list of all card cross references")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross references"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardCrossReferenceResponseDto>> getAllCardCrossReferences(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all card cross references with pagination: {}", pageable);
        Page<CardCrossReferenceResponseDto> cardCrossReferences = cardCrossReferenceService.getAllCardCrossReferences(pageable);
        return ResponseEntity.ok(cardCrossReferences);
    }

    @Operation(summary = "Get card cross reference by account ID", description = "Retrieve a card cross reference by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card cross reference"),
        @ApiResponse(responseCode = "404", description = "Card cross reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<CardCrossReferenceResponseDto> getCardCrossReferenceById(@PathVariable Long accountId) {
        log.info("Retrieving card cross reference by account ID: {}", accountId);
        return cardCrossReferenceService.getCardCrossReferenceById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new card cross reference", description = "Create a new card cross reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card cross reference created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardCrossReferenceResponseDto> createCardCrossReference(@RequestBody CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross reference for account ID: {}", request.getAccountId());
        CardCrossReferenceResponseDto response = cardCrossReferenceService.createCardCrossReference(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing card cross reference", description = "Update card cross reference by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card cross reference updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card cross reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<CardCrossReferenceResponseDto> updateCardCrossReference(@PathVariable Long accountId,
                                                                                  @RequestBody UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross reference for account ID: {}", accountId);
        CardCrossReferenceResponseDto response = cardCrossReferenceService.updateCardCrossReference(accountId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a card cross reference", description = "Delete a card cross reference by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card cross reference deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card cross reference not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteCardCrossReference(@PathVariable Long accountId) {
        log.info("Deleting card cross reference for account ID: {}", accountId);
        cardCrossReferenceService.deleteCardCrossReference(accountId);
        return ResponseEntity.noContent().build();
    }
}
