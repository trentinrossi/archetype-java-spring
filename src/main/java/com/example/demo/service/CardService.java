package com.example.demo.service;

import com.example.demo.dto.CardDTO;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private final CardRepository cardRepository;
    
    @Transactional(readOnly = true)
    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CardDTO getCardByNumber(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
            .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));
        return convertToDTO(card);
    }
    
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByAccountId(String accountId) {
        return cardRepository.findByAccountId(accountId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByStatus(String activeStatus) {
        return cardRepository.findByActiveStatus(activeStatus).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public CardDTO createCard(CardDTO cardDTO) {
        Card card = convertToEntity(cardDTO);
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Transactional
    public CardDTO updateCard(String cardNumber, CardDTO cardDTO) {
        Card existingCard = cardRepository.findById(cardNumber)
            .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));
        
        existingCard.setAccountId(cardDTO.getAccountId());
        existingCard.setCvvCode(cardDTO.getCvvCode());
        existingCard.setEmbossedName(cardDTO.getEmbossedName());
        existingCard.setExpirationDate(cardDTO.getExpirationDate());
        existingCard.setActiveStatus(cardDTO.getActiveStatus());
        
        Card updatedCard = cardRepository.save(existingCard);
        return convertToDTO(updatedCard);
    }
    
    @Transactional
    public void deleteCard(String cardNumber) {
        if (!cardRepository.existsById(cardNumber)) {
            throw new RuntimeException("Card not found with number: " + cardNumber);
        }
        cardRepository.deleteById(cardNumber);
    }
    
    private CardDTO convertToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setAccountId(card.getAccountId());
        dto.setCvvCode(card.getCvvCode());
        dto.setEmbossedName(card.getEmbossedName());
        dto.setExpirationDate(card.getExpirationDate());
        dto.setActiveStatus(card.getActiveStatus());
        return dto;
    }
    
    private Card convertToEntity(CardDTO dto) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setAccountId(dto.getAccountId());
        card.setCvvCode(dto.getCvvCode());
        card.setEmbossedName(dto.getEmbossedName());
        card.setExpirationDate(dto.getExpirationDate());
        card.setActiveStatus(dto.getActiveStatus());
        return card;
    }
}
