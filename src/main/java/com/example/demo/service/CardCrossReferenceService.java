package com.example.demo.service;

import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.repository.CardCrossReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardCrossReferenceService {
    
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    
    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card-to-account mapping for card number: {}", request.getCardNumber());
        
        if (cardCrossReferenceRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card number already mapped to an account");
        }
        
        CardCrossReference cardCrossReference = new CardCrossReference();
        cardCrossReference.setCardNumber(request.getCardNumber());
        cardCrossReference.setAccountId(request.getAccountId());
        cardCrossReference.setCustomerId(request.getCustomerId());
        
        CardCrossReference saved = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Card-to-account mapping created successfully");
        
        return convertToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(String cardNumber) {
        log.info("Retrieving card-to-account mapping for card number: {}", cardNumber);
        return cardCrossReferenceRepository.findByCardNumber(cardNumber)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByAccountId(Long accountId) {
        log.info("Retrieving card-to-account mappings for account ID: {}", accountId);
        return cardCrossReferenceRepository.findByAccountId(accountId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteCardCrossReference(String cardNumber) {
        log.info("Deleting card-to-account mapping: {}", cardNumber);
        CardCrossReference xref = cardCrossReferenceRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card cross reference not found"));
        cardCrossReferenceRepository.delete(xref);
        log.info("Card-to-account mapping deleted successfully");
    }
    
    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference cardCrossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setCardNumber(cardCrossReference.getCardNumber());
        response.setAccountId(cardCrossReference.getAccountId());
        response.setCustomerId(cardCrossReference.getCustomerId());
        response.setCreatedAt(cardCrossReference.getCreatedAt());
        response.setUpdatedAt(cardCrossReference.getUpdatedAt());
        return response;
    }
}
