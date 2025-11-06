package com.example.demo.service;

import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.repository.CardCrossReferenceRepository;
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
public class CardCrossReferenceService {
    
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    
    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross reference with card number: {}", request.getCardNumber());
        
        validateCardNumber(request.getCardNumber());
        
        if (cardCrossReferenceRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card cross reference with card number already exists");
        }
        
        CardCrossReference cardCrossReference = new CardCrossReference();
        cardCrossReference.setCardNumber(request.getCardNumber());
        cardCrossReference.setCrossReferenceData(request.getCrossReferenceData());
        
        CardCrossReference savedCardCrossReference = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Successfully created card cross reference with ID: {}", savedCardCrossReference.getId());
        return convertToResponse(savedCardCrossReference);
    }
    
    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceById(Long id) {
        log.debug("Retrieving card cross reference with ID: {}", id);
        return cardCrossReferenceRepository.findById(id).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(String cardNumber) {
        log.debug("Retrieving card cross reference with card number: {}", cardNumber);
        validateCardNumber(cardNumber);
        return cardCrossReferenceRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }
    
    @Transactional
    public CardCrossReferenceResponseDto updateCardCrossReference(Long id, UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross reference with ID: {}", id);
        
        CardCrossReference cardCrossReference = cardCrossReferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card cross reference not found"));
        
        if (request.getCrossReferenceData() != null) {
            cardCrossReference.setCrossReferenceData(request.getCrossReferenceData());
        }
        
        CardCrossReference updatedCardCrossReference = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Successfully updated card cross reference with ID: {}", id);
        return convertToResponse(updatedCardCrossReference);
    }
    
    @Transactional
    public void deleteCardCrossReference(Long id) {
        log.info("Deleting card cross reference with ID: {}", id);
        if (!cardCrossReferenceRepository.existsById(id)) {
            throw new IllegalArgumentException("Card cross reference not found");
        }
        cardCrossReferenceRepository.deleteById(id);
        log.info("Successfully deleted card cross reference with ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.debug("Retrieving all card cross references with pagination");
        return cardCrossReferenceRepository.findAllOrderByCardNumber(pageable).map(this::convertToResponse);
    }
    
    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
    }
    
    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference cardCrossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setId(cardCrossReference.getId());
        response.setCardNumber(cardCrossReference.getCardNumber());
        response.setCrossReferenceData(cardCrossReference.getCrossReferenceData());
        response.setCreatedAt(cardCrossReference.getCreatedAt());
        response.setUpdatedAt(cardCrossReference.getUpdatedAt());
        return response;
    }
}