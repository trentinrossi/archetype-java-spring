package com.example.demo.service;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    
    private final CardRepository cardRepository;
    
    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card with card number: {}", request.getCardNumber());
        
        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card with this number already exists");
        }
        
        Card card = new Card();
        card.setCardNumber(request.getCardNumber());
        card.setStatus(request.getStatus());
        card.setCardDetails(request.getCardDetails());
        
        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully: {}", savedCard.getCardNumber());
        return convertToResponse(savedCard);
    }
    
    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardByCardNumber(String cardNumber) {
        log.info("Retrieving card by card number: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    @Transactional
    public void deleteCard(String cardNumber) {
        log.info("Deleting card: {}", cardNumber);
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardNumber));
        cardRepository.delete(card);
        log.info("Card deleted successfully: {}", cardNumber);
    }
    
    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setCardNumber(card.getCardNumber());
        response.setStatus(card.getStatus());
        response.setCardDetails(card.getCardDetails());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
