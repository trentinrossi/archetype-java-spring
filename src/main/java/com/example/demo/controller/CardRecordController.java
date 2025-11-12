package com.example.demo.controller;

import com.example.demo.dto.CreateCardRecordRequestDto;
import com.example.demo.dto.UpdateCardRecordRequestDto;
import com.example.demo.dto.CardRecordResponseDto;
import com.example.demo.service.CardRecordService;
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
@Tag(name = "Card Record Management", description = "APIs for managing card records")
@RequestMapping("/api/cardrecords")
public class CardRecordController {

    private final CardRecordService cardRecordService;

    @Operation(summary = "Get all card records", description = "Retrieve a paginated list of all card records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card records"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CardRecordResponseDto>> getAllCardRecords(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CardRecordResponseDto> cardRecords = cardRecordService.getAllCardRecords(pageable);
        return ResponseEntity.ok(cardRecords);
    }

    @Operation(summary = "Get card record by card number", description = "Retrieve a card record by its card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of card record"),
        @ApiResponse(responseCode = "404", description = "Card record not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardRecordResponseDto> getCardRecordByCardNumber(@PathVariable String cardNumber) {
        return cardRecordService.getCardRecordByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new card record", description = "Create a new card record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CardRecordResponseDto> createCardRecord(@RequestBody CreateCardRecordRequestDto request) {
        CardRecordResponseDto response = cardRecordService.createCardRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing card record", description = "Update card record details by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card record updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Card record not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardRecordResponseDto> updateCardRecord(
            @PathVariable String cardNumber,
            @RequestBody UpdateCardRecordRequestDto request) {
        CardRecordResponseDto response = cardRecordService.updateCardRecord(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a card record", description = "Delete a card record by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card record deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card record not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCardRecord(@PathVariable String cardNumber) {
        cardRecordService.deleteCardRecord(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
