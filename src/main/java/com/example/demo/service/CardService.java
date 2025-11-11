package com.example.demo.service;

import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.dto.CardResponseDto;
import com.example.demo.entity.Card;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Account;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.AccountRepository;
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
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card with number: {}", request.getCardNumber());

        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card number must be 16 characters alphanumeric");
        }

        Customer customer = customerRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer ID must be 9 digits numeric and exist in customer file"));

        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file"));

        Card card = new Card();
        card.setCardNumber(request.getCardNumber());
        card.setCustomer(customer);
        card.setAccount(account);

        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully with number: {}", savedCard.getCardNumber());
        return convertToResponse(savedCard);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardByCardNumber(String cardNumber) {
        log.info("Retrieving card by card number: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    @Transactional
    public CardResponseDto updateCard(String cardNumber, UpdateCardRequestDto request) {
        log.info("Updating card with number: {}", cardNumber);

        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer ID must be 9 digits numeric and exist in customer file"));
            card.setCustomer(customer);
        }

        if (request.getAccountId() != null) {
            Account account = accountRepository.findByAccountId(request.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file"));
            card.setAccount(account);
        }

        Card updatedCard = cardRepository.save(card);
        log.info("Card updated successfully with number: {}", updatedCard.getCardNumber());
        return convertToResponse(updatedCard);
    }

    @Transactional
    public void deleteCard(String cardNumber) {
        log.info("Deleting card with number: {}", cardNumber);
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Card not found");
        }
        cardRepository.deleteById(cardNumber);
        log.info("Card deleted successfully with number: {}", cardNumber);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        log.info("Retrieving all cards with pagination");
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getCardsByCustomerId(Long customerId, Pageable pageable) {
        log.info("Retrieving cards for customer ID: {}", customerId);
        return cardRepository.findByCustomer_CustomerId(customerId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getCardsByAccountId(Long accountId, Pageable pageable) {
        log.info("Retrieving cards for account ID: {}", accountId);
        return cardRepository.findByAccount_AccountId(accountId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public boolean cardExists(String cardNumber) {
        return cardRepository.existsByCardNumber(cardNumber);
    }

    /**
     * BR005: Transaction Table Capacity Limit
     * Check if card can accept more transactions (max 10 per card)
     */
    @Transactional(readOnly = true)
    public boolean canAddTransaction(String cardNumber) {
        log.info("Checking transaction capacity for card: {}", cardNumber);
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        return card.canAddTransaction();
    }

    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setCardNumber(card.getCardNumber());
        response.setCustomerId(card.getCustomer().getCustomerId());
        response.setCustomerName(card.getCustomer().getFullName());
        response.setAccountId(card.getAccount().getAccountId());
        response.setTransactionCount(card.getTransactionCount());
        response.setCanAddTransaction(card.canAddTransaction());
        response.setHasValidLinkage(card.hasValidLinkage());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
