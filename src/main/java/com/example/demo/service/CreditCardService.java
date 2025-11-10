package com.example.demo.service;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.CreditCard;
import com.example.demo.enums.CardStatus;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for CreditCard entity.
 * Implements business logic for credit card operations and filtering rules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardService {
    
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    
    /**
     * Create a new credit card
     * Validates card number (16 digits) and account ID (11 digits)
     * @param request the create credit card request
     * @return the created credit card response
     * @throws IllegalArgumentException if validation fails
     */
    @Transactional
    public CreditCardResponseDto createCreditCard(CreateCreditCardRequestDto request) {
        log.info("Creating new credit card with number: {}", maskCardNumber(request.getCardNumber()));
        
        // Validate card number format (16 digits)
        if (!request.getCardNumber().matches("^\\d{16}$")) {
            log.error("Invalid card number format");
            throw new IllegalArgumentException("Card number must be exactly 16 digits");
        }
        
        // Check if card already exists
        if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
            log.error("Credit card already exists with number: {}", maskCardNumber(request.getCardNumber()));
            throw new IllegalArgumentException("Credit card with this number already exists");
        }
        
        // Validate account ID format (11 digits)
        if (!request.getAccountId().matches("^\\d{11}$")) {
            log.error("Invalid account ID format: {}", request.getAccountId());
            throw new IllegalArgumentException("Account ID must be exactly 11 digits");
        }
        
        // Find or create account
        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseGet(() -> {
                    log.info("Account not found, creating new account: {}", request.getAccountId());
                    Account newAccount = new Account(request.getAccountId());
                    return accountRepository.save(newAccount);
                });
        
        // Validate card status
        try {
            CardStatus.fromCode(request.getCardStatus());
        } catch (IllegalArgumentException e) {
            log.error("Invalid card status code: {}", request.getCardStatus());
            throw new IllegalArgumentException("Invalid card status code: " + request.getCardStatus());
        }
        
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setAccount(account);
        creditCard.setCardStatus(request.getCardStatus());
        creditCard.setCardholderName(request.getCardholderName());
        creditCard.setExpiryMonth(request.getExpiryMonth());
        creditCard.setExpiryYear(request.getExpiryYear());
        creditCard.setCardType(request.getCardType());
        creditCard.setCreditLimit(request.getCreditLimit());
        creditCard.setAvailableCredit(request.getAvailableCredit() != null ? 
                request.getAvailableCredit() : request.getCreditLimit());
        
        CreditCard savedCard = creditCardRepository.save(creditCard);
        log.info("Credit card created successfully: {}", maskCardNumber(savedCard.getCardNumber()));
        
        return convertToResponse(savedCard);
    }
    
    /**
     * Get credit card by card number
     * @param cardNumber the 16-digit card number
     * @return Optional containing the credit card response if found
     */
    @Transactional(readOnly = true)
    public Optional<CreditCardResponseDto> getCreditCardByNumber(String cardNumber) {
        log.debug("Fetching credit card with number: {}", maskCardNumber(cardNumber));
        return creditCardRepository.findByCardNumber(cardNumber)
                .map(this::convertToResponse);
    }
    
    /**
     * Get all credit cards with pagination
     * BR001: Admin users can view all cards when no context is passed
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getAllCreditCards(Pageable pageable) {
        log.debug("Fetching all credit cards with pagination");
        return creditCardRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit cards by account ID
     * BR001: Non-admin users can only view cards associated with their specific account
     * BR007: Records are filtered based on account ID criteria when specified
     * @param accountId the account identifier
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByAccountId(String accountId, Pageable pageable) {
        log.debug("Fetching credit cards for account: {}", accountId);
        
        // BR003: Validate account ID format
        if (!accountId.matches("^\\d{11}$")) {
            log.error("Invalid account ID format: {}", accountId);
            throw new IllegalArgumentException("Account ID must be exactly 11 digits");
        }
        
        return creditCardRepository.findByAccountId(accountId, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit cards by status
     * @param cardStatus the status code
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByStatus(String cardStatus, Pageable pageable) {
        log.debug("Fetching credit cards with status: {}", cardStatus);
        
        // Validate card status
        try {
            CardStatus.fromCode(cardStatus);
        } catch (IllegalArgumentException e) {
            log.error("Invalid card status code: {}", cardStatus);
            throw new IllegalArgumentException("Invalid card status code: " + cardStatus);
        }
        
        return creditCardRepository.findByCardStatus(cardStatus, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit cards by account ID and status
     * BR007: Both filters can be applied simultaneously
     * @param accountId the account identifier
     * @param cardStatus the status code
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByAccountIdAndStatus(String accountId, 
                                                                          String cardStatus, 
                                                                          Pageable pageable) {
        log.debug("Fetching credit cards for account: {} with status: {}", accountId, cardStatus);
        
        // Validate account ID format
        if (!accountId.matches("^\\d{11}$")) {
            log.error("Invalid account ID format: {}", accountId);
            throw new IllegalArgumentException("Account ID must be exactly 11 digits");
        }
        
        // Validate card status
        try {
            CardStatus.fromCode(cardStatus);
        } catch (IllegalArgumentException e) {
            log.error("Invalid card status code: {}", cardStatus);
            throw new IllegalArgumentException("Invalid card status code: " + cardStatus);
        }
        
        return creditCardRepository.findByAccountIdAndCardStatus(accountId, cardStatus, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Search credit cards by card number pattern
     * BR007: Records are filtered based on card number criteria when specified
     * @param cardNumberPattern the card number pattern (can include wildcards)
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> searchCreditCardsByNumber(String cardNumberPattern, Pageable pageable) {
        log.debug("Searching credit cards with pattern: {}", cardNumberPattern);
        
        String pattern = "%" + cardNumberPattern + "%";
        return creditCardRepository.findByCardNumberPattern(pattern, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Search credit cards by account ID and card number pattern
     * BR007: Both filters can be applied simultaneously
     * @param accountId the account identifier
     * @param cardNumberPattern the card number pattern
     * @param pageable pagination information
     * @return page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> searchCreditCardsByAccountIdAndNumber(String accountId,
                                                                             String cardNumberPattern,
                                                                             Pageable pageable) {
        log.debug("Searching credit cards for account: {} with pattern: {}", accountId, cardNumberPattern);
        
        // Validate account ID format
        if (!accountId.matches("^\\d{11}$")) {
            log.error("Invalid account ID format: {}", accountId);
            throw new IllegalArgumentException("Account ID must be exactly 11 digits");
        }
        
        String pattern = "%" + cardNumberPattern + "%";
        return creditCardRepository.findByAccountIdAndCardNumberPattern(accountId, pattern, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Update an existing credit card
     * @param cardNumber the card number
     * @param request the update credit card request
     * @return the updated credit card response
     * @throws IllegalArgumentException if card not found or validation fails
     */
    @Transactional
    public CreditCardResponseDto updateCreditCard(String cardNumber, UpdateCreditCardRequestDto request) {
        log.info("Updating credit card with number: {}", maskCardNumber(cardNumber));
        
        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> {
                    log.error("Credit card not found with number: {}", maskCardNumber(cardNumber));
                    return new IllegalArgumentException("Credit card not found");
                });
        
        // Check if card can be modified
        if (!creditCard.canModify()) {
            log.error("Cannot modify cancelled credit card: {}", maskCardNumber(cardNumber));
            throw new IllegalArgumentException("Cannot modify cancelled credit card");
        }
        
        // Update card status if provided
        if (request.getCardStatus() != null) {
            try {
                CardStatus.fromCode(request.getCardStatus());
                creditCard.setCardStatus(request.getCardStatus());
            } catch (IllegalArgumentException e) {
                log.error("Invalid card status code: {}", request.getCardStatus());
                throw new IllegalArgumentException("Invalid card status code: " + request.getCardStatus());
            }
        }
        
        // Update other fields
        if (request.getCardholderName() != null) creditCard.setCardholderName(request.getCardholderName());
        if (request.getExpiryMonth() != null) creditCard.setExpiryMonth(request.getExpiryMonth());
        if (request.getExpiryYear() != null) creditCard.setExpiryYear(request.getExpiryYear());
        if (request.getCardType() != null) creditCard.setCardType(request.getCardType());
        if (request.getCreditLimit() != null) creditCard.setCreditLimit(request.getCreditLimit());
        if (request.getAvailableCredit() != null) creditCard.setAvailableCredit(request.getAvailableCredit());
        
        CreditCard updatedCard = creditCardRepository.save(creditCard);
        log.info("Credit card updated successfully: {}", maskCardNumber(updatedCard.getCardNumber()));
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Delete a credit card
     * @param cardNumber the card number
     * @throws IllegalArgumentException if card not found
     */
    @Transactional
    public void deleteCreditCard(String cardNumber) {
        log.info("Deleting credit card with number: {}", maskCardNumber(cardNumber));
        
        if (!creditCardRepository.existsByCardNumber(cardNumber)) {
            log.error("Credit card not found with number: {}", maskCardNumber(cardNumber));
            throw new IllegalArgumentException("Credit card not found");
        }
        
        creditCardRepository.deleteById(cardNumber);
        log.info("Credit card deleted successfully: {}", maskCardNumber(cardNumber));
    }
    
    /**
     * Get count of credit cards by account ID
     * @param accountId the account identifier
     * @return count of credit cards
     */
    @Transactional(readOnly = true)
    public long countCreditCardsByAccountId(String accountId) {
        log.debug("Counting credit cards for account: {}", accountId);
        return creditCardRepository.countByAccountId(accountId);
    }
    
    /**
     * Convert CreditCard entity to CreditCardResponseDto
     * @param creditCard the credit card entity
     * @return the credit card response DTO
     */
    private CreditCardResponseDto convertToResponse(CreditCard creditCard) {
        CreditCardResponseDto response = new CreditCardResponseDto();
        response.setCardNumber(creditCard.getCardNumber());
        response.setMaskedCardNumber(creditCard.getMaskedCardNumber());
        response.setAccountId(creditCard.getAccountId());
        response.setCardStatus(creditCard.getCardStatus());
        response.setCardStatusEnum(creditCard.getCardStatusEnum());
        response.setCardStatusDisplayName(creditCard.getCardStatusEnum() != null ? 
                creditCard.getCardStatusEnum().getDisplayName() : null);
        response.setCardholderName(creditCard.getCardholderName());
        response.setExpiryMonth(creditCard.getExpiryMonth());
        response.setExpiryYear(creditCard.getExpiryYear());
        response.setCardType(creditCard.getCardType());
        response.setCreditLimit(creditCard.getCreditLimit());
        response.setAvailableCredit(creditCard.getAvailableCredit());
        response.setIsActive(creditCard.isActive());
        response.setIsExpired(creditCard.isExpired());
        response.setCanModify(creditCard.canModify());
        response.setCreatedAt(creditCard.getCreatedAt());
        response.setUpdatedAt(creditCard.getUpdatedAt());
        return response;
    }
    
    /**
     * Mask card number for logging (shows last 4 digits)
     * @param cardNumber the card number
     * @return masked card number
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "*".repeat(cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
    }
}
