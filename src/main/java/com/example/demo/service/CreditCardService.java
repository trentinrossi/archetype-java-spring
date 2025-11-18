package com.example.demo.service;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.User;
import com.example.demo.enums.UserType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreditCardResponseDto createCreditCard(CreateCreditCardRequestDto request) {
        log.info("Creating new credit card with number: {}", maskCardNumber(request.getCardNumber()));
        
        // Validate card number
        validateCardNumber(request.getCardNumber());
        
        if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Credit card with this number already exists");
        }
        
        // Validate and retrieve account
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.getAccountId()));
        
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setAccount(account);
        creditCard.setEmbossedName(request.getEmbossedName());
        creditCard.setExpirationDate(request.getExpirationDate());
        creditCard.setCardStatus(request.getCardStatus());
        creditCard.setActiveStatus(request.getCardStatus());
        creditCard.setCvvCode(request.getCvvCode());
        
        CreditCard savedCard = creditCardRepository.save(creditCard);
        return convertToResponse(savedCard);
    }

    @Transactional(readOnly = true)
    public Optional<CreditCardResponseDto> getCreditCardByNumber(String cardNumber) {
        log.info("Retrieving credit card with number: {}", maskCardNumber(cardNumber));
        
        // BR004: Card Record Retrieval - using card number as primary key
        validateCardNumber(cardNumber);
        
        return creditCardRepository.findByCardNumber(cardNumber)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getAllCreditCards(Pageable pageable) {
        log.info("Retrieving all credit cards with pagination");
        return creditCardRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByAccountId(Long accountId, Pageable pageable) {
        log.info("Retrieving credit cards for account ID: {}", accountId);
        
        // Validate account ID
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }
        
        return creditCardRepository.findByAccount_AccountId(accountId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * BR001: Card Search and Retrieval
     * Users must provide both account ID and card number to search for and retrieve card details
     */
    @Transactional(readOnly = true)
    public CreditCardResponseDto searchCardDetails(Long accountId, String cardNumber, Long userId) {
        log.info("Searching card details - Account ID: {}, Card Number: {}, User ID: {}", 
                 accountId, maskCardNumber(cardNumber), userId);
        
        // BR003: Search Criteria Requirement - both parameters must be provided
        if (accountId == null || cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Both account ID and card number must be provided");
        }
        
        // Validate inputs
        validateCardNumber(cardNumber);
        
        // BR001: User Authorization for Card Viewing
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!user.canViewAccount(accountId)) {
            throw new SecurityException("Access denied: You can only view cards associated with your account");
        }
        
        // BR004: Card Record Retrieval
        CreditCard card = creditCardRepository.findByAccountIdAndCardNumber(accountId, cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account/card combination not found"));
        
        return convertToResponse(card);
    }

    /**
     * BR003: Search Criteria Requirement & BR008: Record Filtering Logic
     * At least one search criterion must be provided to perform a search
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> searchCards(Long accountId, String cardNumber, Long userId, Pageable pageable) {
        log.info("Searching cards - Account ID: {}, Card Number: {}, User ID: {}", 
                 accountId, maskCardNumber(cardNumber), userId);
        
        // BR003: Search Criteria Requirement
        if ((accountId == null || accountId == 0) && (cardNumber == null || cardNumber.trim().isEmpty())) {
            throw new IllegalArgumentException("At least one search criterion must be provided (account number or card number)");
        }
        
        // Validate card number if provided
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            validateCardNumber(cardNumber);
        }
        
        // BR001: User Authorization for Card Viewing
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Page<CreditCard> cards = creditCardRepository.searchCards(accountId, cardNumber, pageable);
        
        // Apply user authorization filtering
        if (user.getUserType() == UserType.REGULAR) {
            // Regular users can only see their own account's cards
            cards = cards.map(card -> {
                if (!user.canViewAccount(card.getAccount().getAccountId())) {
                    throw new SecurityException("Access denied: You can only view cards associated with your account");
                }
                return card;
            });
        }
        
        return cards.map(this::convertToResponse);
    }

    /**
     * BR002: Card Detail Modification
     * Users can modify specific card fields including name, status, and expiration date
     */
    @Transactional
    public CreditCardResponseDto updateCreditCard(String cardNumber, UpdateCreditCardRequestDto request, String updatedBy) {
        log.info("Updating credit card with number: {}", maskCardNumber(cardNumber));
        
        validateCardNumber(cardNumber);
        
        CreditCard card = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit card not found"));
        
        // BR003: Concurrent Update Prevention - check version for optimistic locking
        if (request.getVersion() != null && card.hasBeenModifiedSince(request.getVersion())) {
            log.error("Concurrent update detected - card was modified by another user");
            throw new ObjectOptimisticLockingFailureException(CreditCard.class, cardNumber);
        }
        
        // BR002: Only allow modification of specific fields
        if (request.getEmbossedName() != null) {
            card.setEmbossedName(request.getEmbossedName());
        }
        if (request.getCardStatus() != null) {
            card.setCardStatus(request.getCardStatus());
            card.setActiveStatus(request.getCardStatus());
        }
        if (request.getExpirationDate() != null) {
            card.setExpirationDate(request.getExpirationDate());
        }
        
        card.setLastModifiedBy(updatedBy != null ? updatedBy : request.getUpdatedBy());
        
        // BR004: Update Confirmation Requirement - handled by controller/UI
        CreditCard updatedCard = creditCardRepository.save(card);
        
        log.info("Credit card updated successfully by user: {}", updatedBy);
        return convertToResponse(updatedCard);
    }

    @Transactional
    public void deleteCreditCard(String cardNumber) {
        log.info("Deleting credit card with number: {}", maskCardNumber(cardNumber));
        
        validateCardNumber(cardNumber);
        
        if (!creditCardRepository.existsByCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Credit card not found");
        }
        
        creditCardRepository.deleteById(cardNumber);
    }

    /**
     * BR007: Input Error Highlighting
     * Validate all inputs and throw detailed error messages
     */
    private void validateCardNumber(String cardNumber) {
        log.debug("Validating card number");
        
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            log.error("Card number is empty");
            throw new IllegalArgumentException("Card number must be provided and must be 16 digits");
        }
        
        if (!cardNumber.matches("\\d{16}")) {
            log.error("Card number format invalid: {}", maskCardNumber(cardNumber));
            throw new IllegalArgumentException("CARD NUMBER MUST BE 16 DIGITS");
        }
        
        if (cardNumber.equals("0000000000000000")) {
            log.error("Card number is all zeros");
            throw new IllegalArgumentException("Please enter a valid card number");
        }
    }

    /**
     * BR006: Return from List Screen
     * Maintain search criteria when returning from list screen
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCardsWithPreviousCriteria(Long accountId, String cardNumber, 
                                                                      Long userId, Pageable pageable) {
        log.info("Retrieving cards with previous search criteria - Account ID: {}, Card Number: {}", 
                 accountId, maskCardNumber(cardNumber));
        
        // Maintain previous search values
        return searchCards(accountId, cardNumber, userId, pageable);
    }

    /**
     * BR003: Single Selection Enforcement
     * Ensure only one credit card can be selected for action at a time
     */
    public void validateSingleSelection(List<String> selectedCardNumbers) {
        if (selectedCardNumbers == null || selectedCardNumbers.isEmpty()) {
            throw new IllegalArgumentException("No card selected");
        }
        
        if (selectedCardNumbers.size() > 1) {
            throw new IllegalArgumentException("Only one credit card can be selected at a time");
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private CreditCardResponseDto convertToResponse(CreditCard card) {
        CreditCardResponseDto response = new CreditCardResponseDto();
        response.setCardNumber(card.getCardNumber());
        response.setFormattedCardNumber(card.getFormattedCardNumber());
        response.setMaskedCardNumber(card.getMaskedCardNumber());
        response.setAccountId(card.getAccount().getAccountId());
        response.setEmbossedName(card.getEmbossedName());
        response.setExpirationDate(card.getExpirationDate());
        response.setExpirationDateFormatted(card.getExpirationDateFormatted());
        response.setExpiryMonth(card.getExpiryMonth());
        response.setExpiryYear(card.getExpiryYear());
        response.setExpirationMonth(card.getExpirationMonth());
        response.setExpirationYear(card.getExpirationYear());
        response.setExpirationDay(card.getExpirationDay());
        response.setActiveStatus(card.getActiveStatus());
        response.setCardStatus(card.getCardStatus());
        response.setIsActive(card.isActive());
        response.setIsExpired(card.isExpired());
        response.setCvvCode("***"); // Masked for security
        response.setVersion(card.getVersion());
        response.setLastModifiedBy(card.getLastModifiedBy());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
