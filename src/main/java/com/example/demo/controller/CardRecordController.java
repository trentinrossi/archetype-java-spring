package com.example.demo.controller;

import com.example.demo.dto.CardRecordResponseDto;
import com.example.demo.dto.CreateCardRecordRequestDto;
import com.example.demo.dto.UpdateCardRecordRequestDto;
import com.example.demo.service.CardRecordService;
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
@RequestMapping("/api/card-records")
public class CardRecordController {

    private final CardRecordService cardRecordService;

    @GetMapping
    public ResponseEntity<Page<CardRecordResponseDto>> getAllCardRecords(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all card records with pagination: {}", pageable);
        Page<CardRecordResponseDto> cardRecords = cardRecordService.getAllCardRecords(pageable);
        return ResponseEntity.ok(cardRecords);
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardRecordResponseDto> getCardRecordByNumber(@PathVariable String cardNumber) {
        log.info("Retrieving card record by number: {}", cardNumber);
        return cardRecordService.getCardRecordByNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CardRecordResponseDto> createCardRecord(@Valid @RequestBody CreateCardRecordRequestDto request) {
        log.info("Creating new card record with number: {}", request.getCardNumber());
        CardRecordResponseDto response = cardRecordService.createCardRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardRecordResponseDto> updateCardRecord(
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCardRecordRequestDto request) {
        log.info("Updating card record with number: {}", cardNumber);
        CardRecordResponseDto response = cardRecordService.updateCardRecord(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCardRecord(@PathVariable String cardNumber) {
        log.info("Deleting card record with number: {}", cardNumber);
        cardRecordService.deleteCardRecord(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
