package com.example.demo.service;

import com.example.demo.dto.CardResponseDto;
import com.example.demo.dto.CreateCardRequestDto;
import com.example.demo.dto.UpdateCardRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Card;
import com.example.demo.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    /**
     * BR003: Card Number Validation - Card Number must be numeric and exist in the system
     * BR006: Account and Card Cross-Reference Validation
     */
    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card with number: {}", request.getCardNum());
        
        if (cardRepository.existsByCardNum(request.getCardNum())) {
            throw new IllegalArgumentException("Card Number already exists");
        }
        
        // BR006: Validate account exists
        Account account = accountRepository.findByAcctId(request.getAcctId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID must exist in CXACAIX file"));
        
        Card card = new Card();
        card.setCardNum(request.getCardNum());
        card.setAcctId(request.getAcctId());
        card.setAccount(account);
        
        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully with number: {}", savedCard.getCardNum());
        
        return convertToResponse(savedCard);
    }

    @Transactional(readOnly = true)
    public Optional<CardResponseDto> getCardByNumber(String cardNum) {
        log.info("Retrieving card with number: {}", cardNum);
        
        validateCardNumber(cardNum);
        
        return cardRepository.findByCardNum(cardNum)
                .map(this::convertToResponse);
    }

    @Transactional
    public CardResponseDto updateCard(String cardNum, UpdateCardRequestDto request) {
        log.info("Updating card with number: {}", cardNum);
        
        Card card = cardRepository.findByCardNum(cardNum)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        
        if (request.getAcctId() != null) {
            // BR006: Validate new account exists
            Account account = accountRepository.findByAcctId(request.getAcctId())
                    .orElseThrow(() -> new IllegalArgumentException("Account ID must exist in CXACAIX file"));
            
            card.setAcctId(request.getAcctId());
            card.setAccount(account);
        }
        
        Card updatedCard = cardRepository.save(card);
        return convertToResponse(updatedCard);
    }

    @Transactional
    public void deleteCard(String cardNum) {
        log.info("Deleting card with number: {}", cardNum);
        
        if (!cardRepository.existsByCardNum(cardNum)) {
            throw new IllegalArgumentException("Card not found");
        }
        
        cardRepository.deleteById(cardNum);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * BR003: Card Number Validation - Validate card exists in CCXREF file
     */
    public void validateCardExists(String cardNum) {
        log.info("BR003: Validating card number: {}", cardNum);
        
        validateCardNumber(cardNum);
        
        if (!cardRepository.existsByCardNum(cardNum)) {
            throw new IllegalArgumentException("Card Number must exist in CCXREF file");
        }
    }

    /**
     * BR014: Account-Card Cross-Reference - Get card by account ID
     */
    @Transactional(readOnly = true)
    public Page<CardResponseDto> getCardsByAccountId(Long acctId, Pageable pageable) {
        log.info("BR014: Retrieving cards for account ID: {}", acctId);
        
        return cardRepository.findByAcctId(acctId, pageable)
                .map(this::convertToResponse);
    }

    private void validateCardNumber(String cardNum) {
        if (cardNum == null || cardNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Card Number must be entered");
        }
        if (cardNum.length() != 16) {
            throw new IllegalArgumentException("Card Number must be 16 digits");
        }
        if (!cardNum.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card Number must be numeric");
        }
    }

    private CardResponseDto convertToResponse(Card card) {
        CardResponseDto response = new CardResponseDto();
        response.setCardNum(card.getCardNum());
        response.setAcctId(card.getAcctId());
        response.setTransactionCount(card.getTransactions() != null ? card.getTransactions().size() : 0);
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        
        return response;
    }
}
