package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private final CardRepository cardRepository;
    
    @Transactional(readOnly = true)
    public CardDTO getCardByNumber(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardNumber));
        return mapToDTO(card);
    }
    
    @Transactional(readOnly = true)
    public CardDTO getCardByAccountAndNumber(String accountId, String cardNumber) {
        Card card = cardRepository.findByAccountIdAndCardNumber(accountId, cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found for account: " + accountId + " and card: " + cardNumber));
        return mapToDTO(card);
    }
    
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByAccountId(String accountId) {
        return cardRepository.findByAccountId(accountId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<CardListDTO> getCardsList(String accountId, String cardNumber, Pageable pageable) {
        Page<Card> cards;
        
        if (accountId != null && !accountId.isEmpty()) {
            cards = cardRepository.findByAccountId(accountId, pageable);
        } else if (cardNumber != null && !cardNumber.isEmpty()) {
            cards = cardRepository.findByCardNumber(cardNumber, pageable);
        } else {
            cards = cardRepository.findAll(pageable);
        }
        
        return cards.map(this::mapToListDTO);
    }
    
    @Transactional
    public CardDTO createCard(CardCreateDTO createDTO) {
        if (cardRepository.existsByCardNumber(createDTO.getCardNumber())) {
            throw new RuntimeException("Card already exists: " + createDTO.getCardNumber());
        }
        
        Card card = new Card();
        card.setCardNumber(createDTO.getCardNumber());
        card.setAccountId(createDTO.getAccountId());
        card.setCvvCode(createDTO.getCvvCode());
        card.setEmbossedName(createDTO.getEmbossedName().toUpperCase());
        card.setExpirationDate(createDTO.getExpirationDate());
        card.setActiveStatus(createDTO.getActiveStatus());
        
        Card saved = cardRepository.save(card);
        return mapToDTO(saved);
    }
    
    @Transactional
    public CardDTO updateCard(String cardNumber, CardUpdateDTO updateDTO) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardNumber));
        
        card.setEmbossedName(updateDTO.getEmbossedName().toUpperCase());
        card.setExpirationDate(updateDTO.getExpirationDate());
        card.setActiveStatus(updateDTO.getActiveStatus());
        
        Card saved = cardRepository.save(card);
        return mapToDTO(saved);
    }
    
    @Transactional
    public void deleteCard(String cardNumber) {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new RuntimeException("Card not found: " + cardNumber);
        }
        cardRepository.deleteById(cardNumber);
    }
    
    private CardDTO mapToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setAccountId(card.getAccountId());
        dto.setEmbossedName(card.getEmbossedName());
        dto.setExpirationDate(card.getExpirationDate());
        dto.setActiveStatus(card.getActiveStatus());
        dto.setActive(card.isActive());
        dto.setExpired(card.isExpired());
        return dto;
    }
    
    private CardListDTO mapToListDTO(Card card) {
        CardListDTO dto = new CardListDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setAccountId(card.getAccountId());
        dto.setActiveStatus(card.getActiveStatus());
        return dto;
    }
}
