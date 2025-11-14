package com.example.demo.service;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
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
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card with number: {}", request.getCardNumber());

        validateAccountExists(request.getAccountId());
        validateCustomerExists(request.getCustomerId());

        Card card = new Card();
        card.setCardNumber(request.getCardNumber());
        card.setAccountId(request.getAccountId());
        card.setCustomerId(request.getCustomerId());

        Card saved = cardRepository.save(card);
        log.info("Card created successfully with number: {}", saved.getCardNumber());
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardById(String cardNumber) {
        log.info("Retrieving card with number: {}", cardNumber);
        return cardRepository.findById(cardNumber).map(this::convertToResponse);
    }

    @Transactional
    public CardResponseDto updateCard(String cardNumber, UpdateCardRequestDto request) {
        log.info("Updating card with number: {}", cardNumber);

        Card existing = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Card not found with number: %s", cardNumber)
                ));

        if (request.getAccountId() != null) {
            validateAccountExists(request.getAccountId());
            existing.setAccountId(request.getAccountId());
        }
        if (request.getCustomerId() != null) {
            validateCustomerExists(request.getCustomerId());
            existing.setCustomerId(request.getCustomerId());
        }

        Card updated = cardRepository.save(existing);
        log.info("Card updated successfully with number: {}", cardNumber);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteCard(String cardNumber) {
        log.info("Deleting card with number: {}", cardNumber);
        
        if (!cardRepository.existsById(cardNumber)) {
            throw new IllegalArgumentException(
                String.format("Card not found with number: %s", cardNumber)
            );
        }
        
        cardRepository.deleteById(cardNumber);
        log.info("Card deleted successfully with number: {}", cardNumber);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }

    private void validateAccountExists(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException(
                String.format("Account with ID %d does not exist", accountId)
            );
        }
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException(
                String.format("Customer with ID %d does not exist", customerId)
            );
        }
    }

    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setCardNumber(card.getCardNumber());
        response.setAccountId(card.getAccountId());
        response.setCustomerId(card.getCustomerId());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
