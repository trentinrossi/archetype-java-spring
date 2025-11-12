package com.example.demo.service;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card with card number: {}", maskCardNumber(request.getCardNumber()));

        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card number already exists");
        }

        Card card = new Card();
        card.setCardNumber(request.getCardNumber());
        card.setAccountId(Long.parseLong(request.getAccountId()));
        card.setCustomerId(Long.parseLong(request.getCustomerId()));
        card.setCvvCode(request.getCvvCode());
        card.setExpirationDate(request.getExpirationDate());
        card.setXrefCardNum(request.getXrefCardNum());

        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully");

        return convertToResponse(savedCard);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardByCardNumber(String cardNumber) {
        log.info("Retrieving card by card number");
        return cardRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByAccountId(Long accountId) {
        log.info("Retrieving cards by account ID: {}", accountId);
        return cardRepository.findByAccountId(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByCustomerId(Long customerId) {
        log.info("Retrieving cards by customer ID: {}", customerId);
        return cardRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardResponseDto updateCard(String cardNumber, UpdateCardRequestDto request) {
        log.info("Updating card");

        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (request.getAccountId() != null) {
            card.setAccountId(Long.parseLong(request.getAccountId()));
        }
        if (request.getCustomerId() != null) {
            card.setCustomerId(Long.parseLong(request.getCustomerId()));
        }
        if (request.getCvvCode() != null) {
            card.setCvvCode(request.getCvvCode());
        }
        if (request.getExpirationDate() != null) {
            card.setExpirationDate(request.getExpirationDate());
        }
        if (request.getXrefCardNum() != null) {
            card.setXrefCardNum(request.getXrefCardNum());
        }

        Card updatedCard = cardRepository.save(card);
        log.info("Card updated successfully");

        return convertToResponse(updatedCard);
    }

    @Transactional
    public void deleteCard(String cardNumber) {
        log.info("Deleting card");

        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        cardRepository.delete(card);
        log.info("Card deleted successfully");
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }

    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setCardNumber(card.getCardNumber());
        response.setMaskedCardNumber(card.getMaskedCardNumber());
        response.setFormattedCardNumber(card.getFormattedCardNumber());
        response.setAccountId(String.valueOf(card.getAccountId()));
        response.setCustomerId(String.valueOf(card.getCustomerId()));
        response.setCvvCode("***");
        response.setExpirationDate(card.getExpirationDate());
        response.setXrefCardNum(card.getXrefCardNum());
        response.setIsExpired(card.isExpired());
        response.setCardStatus(card.isExpired() ? "Expired" : "Active");
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
