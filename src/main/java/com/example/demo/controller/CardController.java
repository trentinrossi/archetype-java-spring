package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    
    private final CardService cardService;
    
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardDTO> getCardByNumber(@PathVariable String cardNumber) {
        CardDTO card = cardService.getCardByNumber(cardNumber);
        return ResponseEntity.ok(card);
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardDTO>> getCardsByAccountId(@PathVariable String accountId) {
        List<CardDTO> cards = cardService.getCardsByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/account/{accountId}/card/{cardNumber}")
    public ResponseEntity<CardDTO> getCardByAccountAndNumber(
            @PathVariable String accountId,
            @PathVariable String cardNumber) {
        CardDTO card = cardService.getCardByAccountAndNumber(accountId, cardNumber);
        return ResponseEntity.ok(card);
    }
    
    @GetMapping("/list")
    public ResponseEntity<Page<CardListDTO>> getCardsList(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String cardNumber,
            Pageable pageable) {
        Page<CardListDTO> cards = cardService.getCardsList(accountId, cardNumber, pageable);
        return ResponseEntity.ok(cards);
    }
    
    @PostMapping
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardCreateDTO createDTO) {
        CardDTO created = cardService.createCard(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardDTO> updateCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody CardUpdateDTO updateDTO) {
        CardDTO updated = cardService.updateCard(cardNumber, updateDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        cardService.deleteCard(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
