package com.example.demo.controller;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.service.CardService;
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
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getAllCards(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        Page<CardResponseDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardResponseDto> getCardByCardNumber(@PathVariable String cardNumber) {
        log.info("Retrieving card by card number: {}", cardNumber);
        return cardService.getCardByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<CardResponseDto>> getCardsByCustomerId(
            @PathVariable Long customerId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving cards for customer ID: {}", customerId);
        Page<CardResponseDto> cards = cardService.getCardsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<CardResponseDto>> getCardsByAccountId(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving cards for account ID: {}", accountId);
        Page<CardResponseDto> cards = cardService.getCardsByAccountId(accountId, pageable);
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @Valid @RequestBody CreateCardRequestDto request) {
        log.info("Creating new card with card number: {}", request.getCardNumber());
        CardResponseDto response = cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody UpdateCardRequestDto request) {
        log.info("Updating card with card number: {}", cardNumber);
        CardResponseDto response = cardService.updateCard(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        log.info("Deleting card with card number: {}", cardNumber);
        cardService.deleteCard(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cardNumber}/can-add-transaction")
    public ResponseEntity<Boolean> canAddTransaction(@PathVariable String cardNumber) {
        log.info("Checking transaction capacity for card: {}", cardNumber);
        boolean canAdd = cardService.canAddTransaction(cardNumber);
        return ResponseEntity.ok(canAdd);
    }
}
