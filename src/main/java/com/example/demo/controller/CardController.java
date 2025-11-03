package com.example.demo.controller;

import com.example.demo.dto.CardDTO;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    
    private final CardService cardService;
    
    @GetMapping
    public ResponseEntity<List<CardDTO>> getAllCards() {
        List<CardDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }
    
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
    
    @GetMapping("/status/{activeStatus}")
    public ResponseEntity<List<CardDTO>> getCardsByStatus(@PathVariable String activeStatus) {
        List<CardDTO> cards = cardService.getCardsByStatus(activeStatus);
        return ResponseEntity.ok(cards);
    }
    
    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO) {
        CardDTO createdCard = cardService.createCard(cardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }
    
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardDTO> updateCard(
            @PathVariable String cardNumber,
            @RequestBody CardDTO cardDTO) {
        CardDTO updatedCard = cardService.updateCard(cardNumber, cardDTO);
        return ResponseEntity.ok(updatedCard);
    }
    
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        cardService.deleteCard(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
