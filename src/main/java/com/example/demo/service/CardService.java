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
        log.info("Creating new card with card number: {}", request.getXrefCardNum());

        if (cardRepository.existsByXrefCardNum(request.getXrefCardNum())) {
            throw new IllegalArgumentException("Card with this card number already exists");
        }

        Card card = new Card();
        card.setXrefCardNum(request.getXrefCardNum());
        card.setXrefAcctId(request.getXrefAcctId());

        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully with ID: {}", savedCard.getId());
        return convertToResponse(savedCard);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardById(Long id) {
        log.info("Retrieving card with ID: {}", id);
        return cardRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardByCardNumber(String cardNumber) {
        log.info("Retrieving card with card number: {}", cardNumber);
        return cardRepository.findByXrefCardNum(cardNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByAccountId(String accountId) {
        log.info("Retrieving cards for account ID: {}", accountId);
        List<Card> cards = cardRepository.findByXrefAcctId(accountId);
        return cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public CardResponseDto updateCard(Long id, UpdateCardRequestDto request) {
        log.info("Updating card with ID: {}", id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + id));

        if (request.getXrefCardNum() != null && !request.getXrefCardNum().equals(card.getXrefCardNum())) {
            if (cardRepository.existsByXrefCardNum(request.getXrefCardNum())) {
                throw new IllegalArgumentException("Card with this card number already exists");
            }
            card.setXrefCardNum(request.getXrefCardNum());
        }

        if (request.getXrefAcctId() != null) {
            card.setXrefAcctId(request.getXrefAcctId());
        }

        Card updatedCard = cardRepository.save(card);
        log.info("Card updated successfully with ID: {}", updatedCard.getId());
        return convertToResponse(updatedCard);
    }

    @Transactional
    public void deleteCard(Long id) {
        log.info("Deleting card with ID: {}", id);

        if (!cardRepository.existsById(id)) {
            throw new IllegalArgumentException("Card not found with ID: " + id);
        }

        cardRepository.deleteById(id);
        log.info("Card deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public boolean validateCardAndAccountRelationship(String cardNumber, String accountId) {
        log.info("Validating card {} and account {} relationship", cardNumber, accountId);

        Optional<Card> cardOpt = cardRepository.findByXrefCardNum(cardNumber);

        if (cardOpt.isEmpty()) {
            log.warn("Card not found with card number: {}", cardNumber);
            return false;
        }

        Card card = cardOpt.get();
        boolean isValid = card.getXrefAcctId().equals(accountId);

        if (!isValid) {
            log.warn("Card {} is not associated with account {}", cardNumber, accountId);
        }

        return isValid;
    }

    @Transactional(readOnly = true)
    public boolean isCardValidForBillPayment(String cardNumber, String accountId) {
        log.info("Checking if card {} is valid for bill payment for account {}", cardNumber, accountId);

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            log.warn("Card number is null or empty");
            return false;
        }

        if (accountId == null || accountId.trim().isEmpty()) {
            log.warn("Account ID is null or empty");
            return false;
        }

        Optional<Card> cardOpt = cardRepository.findByXrefCardNum(cardNumber);

        if (cardOpt.isEmpty()) {
            log.warn("Card not found with card number: {}", cardNumber);
            return false;
        }

        Card card = cardOpt.get();
        boolean isValid = card.getXrefAcctId().equals(accountId);

        if (isValid) {
            log.info("Card {} is valid for bill payment for account {}", cardNumber, accountId);
        } else {
            log.warn("Card {} is not valid for bill payment - account mismatch", cardNumber);
        }

        return isValid;
    }

    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setId(card.getId());
        response.setXrefCardNum(card.getXrefCardNum());
        response.setXrefAcctId(card.getXrefAcctId());
        response.setIsValid(card.isValidForBillPayment());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
